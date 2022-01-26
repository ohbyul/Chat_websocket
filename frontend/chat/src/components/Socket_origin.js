import axios from 'axios';
import React, { useEffect, useRef, useState } from 'react';

//https://jcon.tistory.com/186
const style = {border : '1px solid #000' , width:'500px' , height : '300px' , margin : 10 , padding : 10 ,
                overflow :'scroll'}

const Socket = ({roomInfo}) => {
    //send
    const [sendMsg, setSendMsg] = useState('')
    //userName
    const [user , setUser] = useState('')
    const [socketConnected, setSocketConnected] = useState(false);
    const [error , setError] = useState(false)
    const [items, setItems] = useState([]);
    const [userList , setUserList] = useState('')
    const msgRef = useRef(null)

    let ws = useRef(null);
    
    useEffect ( () => {
        console.log("BBBBBBB" , roomInfo);
        axios.get("/chat/room/enter/"+roomInfo.roomId)
        .then( res => {
            console.log("------------------");
            console.log("res" , res.data);
            console.log("------------------");
            
        })
        .catch(error => {
            console.log(error);
        })
    },[roomInfo])



    const webSocketUrl = 'ws://localhost:8080/ws/chat'
    useEffect( () => {
        if (!ws.current) {
            // WebSocket 오브젝트 생성 (자동으로 접속 시작한다. - onopen 함수 호출)
            ws.current = new WebSocket(webSocketUrl)

            // WebSocket 서버와 접속이 되면 호출되는 함수
            ws.current.onopen = (message) => {
                console.log("connected to " + webSocketUrl);
                console.log("message.data " + message);
                // const data = '유저 입장'
                // const text = JSON.stringify(data);
                // setItems(
                //     (prevItems) => [...prevItems,text]
                // )  
                setSocketConnected(true);
                console.log("hi" , message);
                  
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
                if(JSONdata.userName === null || JSONdata.userName.trim() ===''){
                    JSONdata.userName = 'USER'
                }
                const data ='[' + JSONdata.userName + '] : ' + JSONdata.text
                const text = JSON.stringify(data);
                setItems(
                    (prevItems) => [...prevItems, text]
                )      
            }
        }
    },[socketConnected])



    const onSend = () => {
        if(user === null || user.trim() ===''){
            console.log('유저명을 입력해주세요');
            setUser('USER')
        }
        const data = {
            userName : user ,
            roomNo : 1 ,
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
        <div>
            <div>채팅방 이름 : {roomInfo.name} </div>
            <div>socket connected : {`${socketConnected}`}</div>
            <input type='text' value={user} onChange={e => setUser(e.target.value)} />
            <input type='text' value={sendMsg} onChange={e => setSendMsg(e.target.value)} ref={msgRef} />
            <p>
                <button onClick={onSend}>Send</button>
                <button onClick={onDisconnect}>Disconnect</button>
            </p>
            <div style={style}>
                {
                    items.map( (item , index) => {
                        return <div key={index}>{JSON.parse(item)}</div>
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