import React from 'react';
import Main from './components/Main'
import Socket from './components/Socket';
import SocketTest from './components/SocketTest';
function App() {
  return (
    <div>
      <Main/> 
      {/* <SocketTest /> */}
      <Socket />
    </div>
  );
}

export default App;