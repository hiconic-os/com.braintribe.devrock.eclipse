# Headless Eclipse build and publication

## Build

```sh
npm ci --ignore-scripts --prefix tools/deployment
node tools/deployment/build.mjs --work /absolute/path/outside-git/eclipse-build
```

Requires Node 22+, JDK 21+, `hc` and normal Hiconic repository credentials.
The SDK Docker already supplies these. All orchestration is JavaScript; Eclipse's
p2 Publisher and Director execute as Java applications without an Eclipse GUI.

`tools/vendor/eclipse-4.40` contains the pinned binary target platform, approximately
166 MiB. Its inventory and individual file hashes are checked before use. Builds
read these binaries directly and write configuration/output outside the checkout.
No Eclipse SDK download, Python installation or Docker image change is needed.
`baseline.json` records provenance, original archive SHA-512 and inventory SHA-256.
Licenses remain in the distributed bundles. Source and documentation bundles are
excluded. Maintainer imports use `vendor-sdk.mjs` with an extracted original SDK
and the original archive; this is not part of ordinary builds.

Feature templates are tracked under `tools/p2/features`. `modules.json` defines the
seven plugins corresponding to the installed Devrock set: main, artifact container,
model nature, debug module nature, artifact reflection builder, mungo jerry and
file classpath launcher. Legacy plugins are not included.

The build first installs five helper artifacts from this checkout. It then runs
`hc update-classpath --buildFile update.xml` for the six Maven-based plugins in
working copies. The launcher has no Maven libraries. It checks generated classpaths,
compiles Java sources, packages bundles/features and performs a real p2 installation
into an isolated profile. Old `.class` files in source directories are excluded.
The build report records dependency names and hashes. Published versions must be
strictly older than the proposed versions; UTC qualifiers identify each release.
The latest libraries are those selected by the declared Maven version ranges.

## Cumulative website

`website.mjs` snapshots current beta metadata and uses the official p2 Publisher
with `-append`. It preserves old units and artifact versions and generates HTML
and site.xml from the merged feature inventory. Existing old JARs remain on the
server; the candidate contains only new payloads and cumulative metadata.

`publication-plan.json` records previous metadata hashes, retained paths, payloads
and metadata activation order. `sftp-publish.mjs` defaults to a read-only dry run.
With `--apply`, it obtains an exclusive lock, verifies the old server state, uploads
and verifies immutable new payloads, backs up old metadata and activates artifacts
before content metadata using POSIX rename. Old payloads are never overwritten.
The four root metadata files are not a single atomic transaction. A process crash
can leave a lock requiring inspection before removal.

## Workflow and credentials

The manual GitHub workflow has three modes:

- `build`: fresh dependency resolution, compilation, p2 installation check and
  cumulative site artifact; no beta upload.
- `publish`: the same build followed by beta publication.
- `probe`: isolated SFTP upload/rename/download and public HTTPS verification.

No push trigger publishes automatically. The environment is `eclipse-beta`.
Set `ECLIPSE_FTP_PASSWORD` as a secret and `ECLIPSE_FTP_USER` as a variable or secret.
Fresh resolution uses existing `DEVROCK_PIPELINE_UPLOAD_USER/PASSWORD` secrets.

Defaults: SFTP `home29449762.1and1-data.host:22`, beta `/eclipse/beta`, test
`/eclipse/test`, public URLs https://eclipse.hiconic.dev/beta/ and
https://eclipse.hiconic.dev/test/. The SSH key is pinned in the workflow and was
observed during the authenticated test (TOFU). Variables can override these values.
Never put passwords into workflow inputs or command-line arguments.

Locally, `--env-file /absolute/private/file.env` reads only `ECLIPSE_FTP_USER` and
`ECLIPSE_FTP_PASSWORD`, without executing the file. Keep it outside Git with mode
0600. `sftp-probe.mjs` accepts `--host`, `--port`, `--remote-dir`, `--public-base`
and `--host-key` (or a private `--known-hosts` file).

## Validation

On 17 November 2026 the complete Node-only build with the vendored target platform
succeeded: helper builds, six Ant classpath refreshes, seven plugin builds and a
real p2 installation. The candidate retained 118 existing artifact versions and
added 14 payloads; HTML/site.xml represent 68 feature versions. Eleven SFTP tests
cover pinning, cleanup, version collisions, locks and publication failure handling.
The real IONOS SFTP/HTTPS probe and a read-only production publication check also
succeeded. These checks do not replace functional IDE/workspace tests of resolver
behavior. The GitHub workflow and production publication have not yet been run.

## Select beta or beta-2

The workflow input `variant` selects `beta-2` (default) or `beta`. Both the
metadata snapshot and the SFTP destination use the selected site. For the first
isolated test, finish copying the entire beta folder to beta-2, then choose
`mode=publish`, `variant=beta-2`. The probe mode still uses `/eclipse/test`.
Both variants use the existing `eclipse-beta` credentials. Optional variables
`ECLIPSE_BETA_2_REMOTE_DIR` and `ECLIPSE_BETA_2_PUBLIC_URL` override the test paths;
existing `ECLIPSE_BETA_*` overrides apply only to beta. Locally pass
`--published-site https://eclipse.hiconic.dev/beta-2/` to `build.mjs`.

## Scanner regression check

Every plugin build also compiles and runs `tools/tests/ScannerDirectoryListingTest.java`
against the real compiled source and local-metadata scanners. It covers missing roots,
regular-file roots, null directory listings, healthy siblings beside unavailable
subdirectories, and job completion after a worker exception. Each scan has a timeout
so the previous hang fails the build. Results are written to `build/logs/scanner-test.log`.
To rerun against an existing build:

```sh
node tools/deployment/scanner-test.mjs --build /absolute/path/eclipse-build/build
```
