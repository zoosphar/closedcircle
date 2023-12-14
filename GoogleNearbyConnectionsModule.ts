import {NativeModules} from 'react-native';

const {GoogleNearbyConnectionsModule} = NativeModules;

export default {
  startDiscovery: GoogleNearbyConnectionsModule.startDiscovery,
  startAdvertising: GoogleNearbyConnectionsModule.startAdvertising,
};
