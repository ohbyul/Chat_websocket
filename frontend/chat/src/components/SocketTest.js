// import React, { useEffect, useState, useRef } from "react";

// const SocketTest = () => {
//     const [socketConnected, setSocketConnected] = useState(false);
//     const [sendMsg, setSendMsg] = useState(false);
//     const [items, setItems] = useState([]);
//     const [message , setMessage ] = useState('hello')
//     const [error , setError] = useState(false)
//     const [errorValue , setErrorValue] = useState()
//     const webSocketUrl = 'ws://127.0.0.1:8080/websocket'
//     let ws = useRef(null);
    

//     // const callSocket = () => {
//     //   console.log("click btn");
//     //   console.log("ws.current 1 : ",ws.current);

//     //   ws.current = new WebSocket(webSocketUrl);
//     //   console.log("ws.current 2 : ",ws.current);

//     // }

//     // 소켓 객체 생성
//     useEffect(() => {

//         console.log("ws.current 1 : ",ws.current);

//         ws.current = new WebSocket(webSocketUrl);
//         console.log("ws.current 2 : ",ws.current);

//         //WebSocket 서버와 접속이 되면 호출되는 함수
//         ws.current.onopen = (message) => {
//           console.log("connected to " + webSocketUrl);
//           console.log("message to " + message);
//           setSocketConnected(true);
//         };
//         // WebSocket 서버와 접속이 끊기면 호출되는 함수
//         ws.current.onclose = (error) => {
//           console.log("disconnect from " + webSocketUrl);
//           console.log(error);
//         };
//         // WebSocket 서버와 통신 중에 에러가 발생하면 요청되는 함수
//         ws.current.onerror = (error) => {
//           console.log("connection error " + webSocketUrl);
//           console.log(error);
//           setError(true)
//         };
//         // WebSocket 서버로 부터 메시지가 오면 호출되는 함수
//         ws.current.onmessage = (evt) => {
//           const data = JSON.parse(evt.data);
//           console.log(data);
//           setItems((prevItems) => [...prevItems, data]);
//         };

  
//       return () => {
//         console.log("clean up");
//         ws.current.close();
//       };
//     }, [socketConnected]);
  
//     // 소켓이 연결되었을 시에 send 메소드
//     useEffect(() => {
//       if (socketConnected) {
//         ws.current.send(
//           JSON.stringify({
//             message: message,
//           })
//         );
  
//         setSendMsg(true);
//       }
//     }, [socketConnected]);
  

//     return (
//         <div>
//             <h2>Wheb socket</h2>
//             <div>socket</div>
//             <div>socket connected : {`${socketConnected}`}</div>
//             <button onClick={callSocket}>소켓 호출 </button>
//             <div>res : </div>
//             <div>
//                 {/* {items.map((item) => {
//                 return <div>{JSON.stringify(item)}</div>;
//                 })} */}
//                 {
//                     error ? `error` : ''
//                 }
//             </div>
//         </div>
//     );
// };

// export default SocketTest;