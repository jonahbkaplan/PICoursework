import { CapacitorConfig } from '@capacitor/cli';

const config: CapacitorConfig = {
  appId: 'com.focus.app',
  appName: 'focus',
  webDir: 'build',
  server: {
    androidScheme: 'https'
  }
};

export default config;
