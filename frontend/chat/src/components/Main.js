import React, { useEffect, useState } from 'react';
import axios from "axios";

const Main = () => {
    const [data , setData] = useState([])

    const getData = () => {
        axios.get("/api/test")
            .then( res => {
                // console.log("res" , res);
                setData(res)
            })
            .catch(
                // console.log("error")
            )
    }

    useEffect( ()=> {
        getData()
        console.log("Main components on");
        
    },[])

    return (
        <div>
            <h2>Main</h2>
            <p>{data.status}</p>
            <hr /> 
            
        </div>
    );
};

export default Main;