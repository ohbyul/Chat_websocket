import axios from 'axios';
import React, { useEffect, useMemo, useRef, useState } from 'react';
import Socket from './Socket';

const RoomList = () => {
    const [roomName , setRoomNane] = useState('')
    const [roomList , setRoomList] = useState([])
    const [roomInfo , setRoomInfo] =useState('')
    const [ sender , setSender ] = useState('')
    const [ loading, setLoading ] = useState(true)
    const roomRef = useRef(null)
    useEffect( ()=> {
        // getData()
        console.log("roomLst.js -> useEffect on");
        axios.get("/chat/rooms")
            .then(res => {
                console.log("채팅 리스트 /chat/rooms",res.data);
                setRoomList(res.data)
            })
            .catch(error => {
                console.log(error);
            })
        
    },[loading])
    //자동 렌더링 전 roomList 넣으면 무한 반복..

    const onAdd = () =>{
        if(roomName === "" || roomName === null){
            alert("방 제목을 입력해 주세요.")
            return
        }else{
            axios.get("/chat/room?name="+roomName)
            .then( res => {
                console.log("------------------");
                console.log("채팅방 생성 결과값" , res);
                console.log("------------------");
                alert(res.data.name+"방 개설에 성공하였습니다.")
                setRoomNane('')
                roomRef.current.focus()
                setLoading(!loading)
            })
            .catch(error => {
                console.log(error);
            })
        }
    }

    const onChat = (roomInfo) => {
        const nick = prompt('대화명을 입력해 주세요.');
        if(nick===null || nick === ''){
            alert("대화방 입장을 취소하였습니다.")
        }else{
            alert("대화방을 입장합니다.")
            setRoomInfo(roomInfo)
            setSender(nick)
        }
    }


    return (
        <div>
            <div>채팅방</div>
            <div>
                <label>방제목</label>
                <input type="text" value={roomName} onChange={e => setRoomNane(e.target.value)} ref={roomRef}/>
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