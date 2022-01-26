import axios from 'axios';
import React, { useEffect, useRef, useState } from 'react';

//https://jcon.tistory.com/186
const style = {border : '1px solid #000' , width:'500px' , height : '300px' , margin : 10 , padding : 10 ,
                overflow :'scroll'}

const Socket = ({roomInfo,sender}) => {
    //send
    const [sendMsg, setSendMsg] = useState('')
    //userName
    const [user , setUser] = useState(sender)
    const [socketConnected, setSocketConnected] = useState(false);
    const [error , setError] = useState(false)
    const [items, setItems] = useState([]);
    const [userList , setUserList] = useState('')
    const msgRef = useRef(null)

    let ws = useRef(null);
    
    useEffect ( () => {
        console.log("BBBBBBB" , roomInfo);
        const path = "/room/" + roomInfo.roomId
        axios.get( "/chat/room/" + roomInfo.roomId)
            .then( res => {
                console.log("res---",res);
            })
            .catch( error => {
                console.log(error);
            })
        console.log("--------------------------------------");
        
        
        
        // const message = {
        //     type : "ENTER" ,
        //     message : "오별" ,
        //     roomId :roomInfo.roomId , 
        //     sender : sender
        // }
        // // const message = JSON.stringify(data)
        // // console.log(message);
        // axios.get( "/pub/chat/message" ,message)
        // .then( res => {
        //     console.log("res++++++",res);
        // })
        // .catch( error => {
        //     console.log(error);
        // })
    },[roomInfo])



    const webSocketUrl = 'ws://localhost:8080/ws/chat'
    useEffect( () => {
        // if (!ws.current) {
        if (sender !== '') {
            // WebSocket 오브젝트 생성 (자동으로 접속 시작한다. - onopen 함수 호출)
            ws.current = new WebSocket(webSocketUrl)
            
            // WebSocket 서버와 접속이 되면 호출되는 함수
            ws.current.onopen = (message) => {
                console.log("connected to " + webSocketUrl);
                console.log("message 1 : " + message);
                console.log("room info " , roomInfo)
                // const data = '유저 입장'
                // const text = JSON.stringify(data);
                // setItems(
                //     (prevItems) => [...prevItems,text]
                // )  
                setSocketConnected(true)
                console.log("message 2" , message)
                initSend()
            }
            // WebSocket 서버와 접속이 끊기면 호출되는 함수
            ws.current.onclose = (error) => {
                console.log("disconnect from " + webSocketUrl);
                console.log(error);
            }
            // WebSocket 서버와 통신 중에 에러가 발생하면 요청되는 함수
            ws.current.onerror = (error)=> {
                console.log("connection error " + webSocketUrl);
                console.log(error);
                setError(true)
            }
            // WebSocket 서버로 부터 메시지가 오면 호출되는 함수
            ws.current.onmessage = (evt) => {
                const JSONdata = JSON.parse(evt.data)      
                console.log("onMessage",JSONdata);
                const chatData ='[' + JSONdata.sender + '] : ' + JSONdata.message
                console.log("^^^^^^^^^chatData : " , chatData)
                // const text = JSON.stringify(chatData)
                // console.log("text  dddd : " , text)
                setItems(
                    (prevItems) => [...prevItems, chatData]
                )
            }
        }
    },[sender])

    const initSend = () => {
        // if(socketConnected){
            const dataList = {
                roomId : roomInfo.roomId,
                roomName : roomInfo.name , 
                userName : sender , 
                type : "INIT"
            }
            const data = JSON.stringify(dataList)
            console.log("data",data)
            ws.current.send(data)
        // }
    }

    const onSend = () => {
        // if(user === null || user.trim() ===''){
        //     console.log('유저명을 입력해주세요');
        //     setUser('USER')
        // }
        const data = {
            roomId : roomInfo.roomId,
            userName : sender , 
            type : "TALK",
            text : sendMsg 
        }
        const jsonData = JSON.stringify(data)
        console.log("data" , jsonData);
        ws.current.send(jsonData)
        setSendMsg('')
        msgRef.current.focus()
    }
    const onDisconnect = () => {
        ws.current.close()
    }

    return (
        <div style={{margin : 20}}>
            <div>채팅방 이름 : {roomInfo.name} </div>
            <div>socket connected : {`${socketConnected}`}</div>
            {/* <input type='text' value={user} onChange={e => setUser(e.target.value)} /> */}
            <span> {sender} </span>
            <input type='text' value={sendMsg} onChange={e => setSendMsg(e.target.value)} ref={msgRef} />
            <p>
                <button onClick={onSend}>Send</button>
                <button onClick={onDisconnect}>Disconnect</button>
            </p>
            <div style={style}>
                {
                    items.map( (item , index) => {
                        return <div key={index}> {item} </div>
                    } )
                }
            </div>
            <div>
                <p>접속현황</p>
                <div style={{width:"100px",height:"100px",border:'1px solid #000'}}>

                </div>
            </div>
        </div>
    );
};

export default Socket;


/**
 * http://it-archives.com/221339648075/
 * https://nowonbun.tistory.com/285
 */