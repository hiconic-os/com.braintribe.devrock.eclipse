# Eclipse plugin releases

All orchestration uses Node.js. Requirements: Node 22+, JDK 21+ and the Hiconic
`hc` command with repository credentials. The existing Hiconic SDK container has
these runtimes; no Python or Docker image additions are required.

## Publish on GitHub

Run **Eclipse plugins (build, beta publish or SFTP probe)** from Actions on `main`:

- `publish`: resolve fresh Hiconic libraries, build all seven plugins, verify a
  headless p2 installation and publish the cumulative https://eclipse.hiconic.dev/beta/ site.
- `build`: the same fresh build and validation, without uploading to beta.
- `probe`: test SFTP and HTTPS using an isolated file under `/eclipse/test`.

Use **publish** to release plugins with the newest libraries selected by their POM
version ranges. Existing plugin and feature versions remain available. New builds
receive a common UTC qualifier (plus GitHub run number and attempt in CI).
Bundle/feature base versions are independent of Maven POM versions.

The `eclipse-beta` environment needs `ECLIPSE_FTP_PASSWORD` as a secret and
`ECLIPSE_FTP_USER` as a variable or secret. Maven resolution uses the existing
`DEVROCK_PIPELINE_UPLOAD_USER` and `DEVROCK_PIPELINE_UPLOAD_PASSWORD` secrets.

## Local build and test

```sh
npm ci --ignore-scripts --prefix tools/deployment
node tools/deployment/local.mjs
node tools/deployment/site.mjs --root ../../runtime/eclipse-update-site serve --port 8098
```

The local command builds fresh dependencies, validates installation and publishes
an immutable child site under `../../runtime/eclipse-update-site/releases/`.
Existing local releases remain in the composite repository. The HTTP server binds
to loopback only. Its update URL is http://127.0.0.1:8098/.

To install into a **closed test Eclipse**, rather than the personal IDE:

```sh
node tools/deployment/install.mjs --eclipse /path/to/test/eclipse --site http://127.0.0.1:8098/
```

The installer enables local HTTP for its own p2 process. When starting a test IDE
manually, use a separate workspace and `-vmargs -Dp2.httpRule=allow` if needed.

See [deployment details](deployment/README.md).
