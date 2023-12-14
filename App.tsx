import React, {useEffect, useState} from 'react';
import {SafeAreaView, Text} from 'react-native';
import dgram from 'react-native-udp';
import {NetworkInfo} from 'react-native-network-info';
import UdpSocket from 'react-native-udp/lib/types/UdpSocket';
// import { NativeModules } from 'react-native';
import GoogleNearbyConnectionsModule from './GoogleNearbyConnectionsModule';

function App(): JSX.Element {
  const [ipAddress, setIpAddress] = useState<string | undefined>('');
  const [isServer, setIsServer] = useState<boolean>(false);
  const [connectionStatus, setConnectionStatus] = useState<string | null>('');
  const [message, setMessage] = useState<string | null>('');
  const [socket, setSocket] = useState<UdpSocket | null>();

  // const GoogleNearbyConnections = NativeModules.

  useEffect(() => {
    GoogleNearbyConnectionsModule.startAdvertising();
    console.log('hello there');

    // const fetchIpAddress = async () => {
    //   const ip: string = (await NetworkInfo.getIPV4Address()) || '';
    //   setIpAddress(ip);
    // };

    // fetchIpAddress();

    // if (isServer) {
    //   setConnectionStatus(`Servidor desconectado`);
    //   const server = dgram.createSocket({type: 'udp4'});

    //   server.on('message', (data, rinfo) => {
    //     setMessage(data.toString());
    //     console.log('Message received', data.toString());
    //   });

    //   server.on('listening', () => {
    //     console.log('Server listening on port:', server.address().port);
    //     setConnectionStatus(
    //       `Server listening on port ${server.address().port}`,
    //     );
    //   });

    //   server.bind(8888);

    //   setSocket(server);
    // } else {
    //   const client = dgram.createSocket({type: 'udp4'});
    //   client.bind(8887);
    //   setSocket(client);
    // }
    // return () => {
    //   socket && socket.close();
    // };
  }, []);

  const sendMessage = () => {
    if (isServer) return;

    const client = socket;

    client?.send(
      'Hello from the client',
      undefined,
      undefined,
      8888,
      ipAddress,
      error => {
        if (error) {
          console.log('Error sending message', error);
        } else {
          console.log('Message sent successfully');
        }
      },
    );
  };
  return (
    <SafeAreaView>
      <Text>Hello</Text>
    </SafeAreaView>
  );
}

export default App;
