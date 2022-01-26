import React from 'react';
import Main from './components/Main'
import RoomList from './components/RoomList';
import Socket from './components/Socket';
import SocketTest from './components/SocketTest';
function App() {
  return (
    <div>
      <Main/> 
      {/* <SocketTest /> */}
      {/* <Socket /> */}
      <RoomList />
    </div>
  );
}

export default App;