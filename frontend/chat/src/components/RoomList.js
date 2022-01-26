import axios from 'axios';
import React, { useEffect, useMemo, useState } from 'react';
import Socket from './Socket';

const RoomList = () => {
    const [roomName , setRoomNane] = useState('')
    const [roomList , setRoomList] = useState([])
    const [roomInfo , setRoomInfo] =useState('')
    const [ sender , setSender ] = useState('')
    useEffect( ()=> {
        // getData()
        console.log("useEffect on");
        axios.get("/chat/rooms")
            .then(res => {
                console.log(res.data);
                setRoomList(res.data)
            })
            .catch(error => {
                console.log(error);
            })
        
    },[])
    //자동 렌더링 전 

    const onAdd = () =>{
        if(roomName === "" || roomName === null){
            alert("방 제목을 입력해 주세요.")
            return
        }else{
            axios.get("/chat/room?name="+roomName)
            .then( res => {
                console.log("------------------");
                console.log("res" , res);
                console.log("------------------");
                alert(res.data.name+"방 개설에 성공하였습니다.")
                setRoomNane('')
            })
            .catch(error => {
                console.log(error);
            })
        }
    }

    const onChat = (roomInfo) => {
        console.log(roomInfo)
        setRoomInfo(roomInfo)
        const nick = prompt('대화명을 입력해 주세요.');
        console.log(nick);
        setSender(nick)
    }


    return (
        <div>
            <div>채팅방</div>
            <div>
                <label>방제목</label>
                <input type="text" value={roomName} onChange={e => setRoomNane(e.target.value)}/>
                <button onClick={onAdd}>채팅방 개설</button>
            </div>
            <div>
                <label>채팅방 리스트</label>
                <div>
                    <ul>
                        {
                            roomList.map(room => 
                                <li key={room.roomId}> 
                                    {room.name}
                                    <button onClick={ () => onChat(room) }>입장</button>
                                </li>
                            ) 
                        }
                    </ul>
                </div>

                <hr />

                <label>채팅내역</label>
                <div>
                    <Socket roomInfo ={roomInfo} sender={sender}/>
                </div>
            </div>
        </div>
    );
};

export default RoomList;