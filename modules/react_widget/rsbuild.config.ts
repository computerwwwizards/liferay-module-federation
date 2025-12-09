import { defineConfig } from '@rsbuild/core';
import { pluginReact } from '@rsbuild/plugin-react';
import { pluginModuleFederation } from '@module-federation/rsbuild-plugin';
import moduleFederationConfig from './module-federation.config';

// Docs: https://rsbuild.rs/config/
export default defineConfig({
  plugins: [pluginReact(), pluginModuleFederation(moduleFederationConfig)],
  source: {
    entry: {
      index: {
        import: './client/index.ts',
        html: false,
        filename: 'index.js',
        library: {
          type: 'module'
        }
      }
    }
  },

  output: {
    minify: false,
    module: true,
    distPath: './build/resources/main/META-INF/resources/js'
  },
  tools: {
    rspack: {
      optimization: {
        runtimeChunk: false,
        splitChunks: false
      }
    }
  }
});
