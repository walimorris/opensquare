import React, {useEffect, useState} from "react";
import Paper from '@mui/material/Paper';
import {ChatDisplayTextInput} from "./ChatDisplayTextInput";
import { MessageLeft, MessageRight } from "./ChatDisplayMessage";
import FunctionUtil from "../utils/FunctionUtil";

export default function ChatDisplay() {
    const initialPromptResponse = <MessageLeft
        message="How can I assist with your search today?"
        timestamp={FunctionUtil.getCurrentTime()}
        photoURL="https://lh3.googleusercontent.com/a-/AOh14Gi4vkKYlfrbJ0QLJTg_DLjcYyyK7fYoWRpz2r4s=s96-c"
        displayName="Viki"
        avatarDisp={true}
    />

    const [messages, setMessages] = useState([initialPromptResponse]);

    function callBack(childData) {
        console.log("incoming from child")
        console.log(childData.length)
        setMessages(childData);
    }

    useEffect(() => {
        console.log(messages);
    }, [messages]);

    return (
        <div style={{ width: "50vw", height: "100vh", display: "flex", alignItems: "center", justifyContent: "center" }}>
            <Paper>
                <Paper id="style-1" sx={{ width: "calc( 100% - 20px )", margin: 10, overflowY: "scroll", height: "calc( 100% - 80px )" }}>
                    {messages}
                </Paper>
                <ChatDisplayTextInput messages={messages} callBack={callBack}/>
            </Paper>
        </div>
    );
}