import { createModuleFederationConfig } from '@module-federation/rsbuild-plugin';

export default createModuleFederationConfig({
  name: 'mf_project_name',
  remotes: {
  },
  shareStrategy: 'loaded-first',
  shared: {
    react: { singleton: true, eager: true },
    'react-dom': { singleton: true , eager: true},
  },
});
