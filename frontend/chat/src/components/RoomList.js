import axios from 'axios';
import React, { useState } from 'react';

const RoomList = () => {
    const [roomName , setRoomNane] = useState('')
    const [roomList , setRommList] = useState([])

    const onAdd = () =>{
        const name = 'aa'
        axios.post("http://localhost:8080/chat/room",name)
            .then( res => {
                alert(res.data.name+"방 개설에 성공하였습니다.")

            })
            .catch(
                // console.log("error")
            )
    }

    return (
        <div>
            <div>채팅방 리스트</div>
            <div>
                <label>방제목</label>
                <input type="text" value={roomName} onChange={e => setRoomNane(e.target.value)}/>
                <button onClick={onAdd}>채팅방 개설</button>
            </div>
            <div>

            </div>
        </div>
    );
};

export default RoomList;