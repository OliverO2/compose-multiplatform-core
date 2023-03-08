import { instantiate } from './androidx-demo-counting-grid-wasm.uninstantiated.mjs';

await wasmSetup;
instantiate({ skia: Module['asm'] });
