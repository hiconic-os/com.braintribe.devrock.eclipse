// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ============================================================================
import java.io.File;
import java.io.FileFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Comparator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import com.braintribe.devrock.bridge.eclipse.internal.InternalMcBridge;
import com.braintribe.devrock.importer.scanner.SourceScanner;

/** Standalone regression tests against the real scanner classes, without starting an Eclipse UI. */
public class ScannerDirectoryListingTest {
    public static void main(String[] args) {
        try {
            run("source", directory -> new SourceScanner.ScanJob().scan(directory), "pom.xml", """
                    <project><modelVersion>4.0.0</modelVersion><groupId>org.example</groupId>
                    <artifactId>scanner-test</artifactId><version>1.0.0</version></project>
                    """);
            run("metadata", directory -> new InternalMcBridge.ScanJob().scan(directory), "maven-metadata-local.xml", """
                    <metadata><groupId>org.example</groupId><artifactId>scanner-test</artifactId>
                    <versioning><versions><version>1.0.0</version></versions></versioning></metadata>
                    """);
            System.out.println("Scanner directory listing regression tests: 10 passed");
        } catch (Throwable failure) {
            failure.printStackTrace();
            System.exit(1);
        }
    }

    private static void run(String name, Function<File, Collection<?>> scan, String marker, String content) throws Exception {
        Path root = Files.createTempDirectory("scanner-" + name);
        try {
            checkSize(name + ": missing root", () -> scan.apply(root.resolve("missing").toFile()), 0);
            Path regularFile = Files.writeString(root.resolve("file"), "not a directory");
            checkSize(name + ": regular file root", () -> scan.apply(regularFile.toFile()), 0);
            checkSize(name + ": unreadable root", () -> scan.apply(new ListingFile(root.toFile(), null)), 0);

            File unreadableChild = new ListingFile(root.resolve("unreadable-child").toFile(), null);
            Path healthy = Files.createDirectory(root.resolve("healthy"));
            Files.writeString(healthy.resolve(marker), content);
            File mixed = new ListingFile(root.toFile(), new File[] {unreadableChild, healthy.toFile()});
            checkSize(name + ": healthy sibling survives unreadable child", () -> scan.apply(mixed), 1);

            // An unexpected worker failure must still release the job counter, rather than hang the caller.
            File failingChild = new ListingFile(root.resolve("failing-child").toFile(), null) {
                @Override public File[] listFiles(FileFilter filter) {
                    throw new IllegalStateException("simulated directory I/O failure");
                }
            };
            File failedWorker = new ListingFile(root.toFile(), new File[] {failingChild, healthy.toFile()});
            checkSize(name + ": worker failure releases completion", () -> scan.apply(failedWorker), 1);
        } finally {
            try (var paths = Files.walk(root)) {
                for (Path path : paths.sorted(Comparator.reverseOrder()).toList())
                    Files.delete(path);
            }
        }
    }

    private static void checkSize(String name, Callable<Collection<?>> action, int expected) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor(task -> {
            Thread thread = new Thread(task, "scanner-regression-test");
            thread.setDaemon(true);
            return thread;
        });
        try {
            Collection<?> result = executor.submit(action).get(10, TimeUnit.SECONDS);
            if (result.size() != expected)
                throw new AssertionError(name + ": expected " + expected + " artifacts, got " + result.size());
        } finally {
            executor.shutdownNow();
        }
    }

    private static class ListingFile extends File {
        private final File[] children;
        ListingFile(File path, File[] children) {
            super(path.getPath());
            this.children = children;
        }
        @Override public File[] listFiles(FileFilter filter) {
            return children;
        }
    }
}
