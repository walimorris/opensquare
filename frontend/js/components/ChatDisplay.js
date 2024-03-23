import React, {useEffect, useState} from "react";
import Paper from '@mui/material/Paper';
import { MessageLeft, MessageRight } from "./ChatDisplayMessage";
import FunctionUtil from "../utils/FunctionUtil";
import axios from "axios";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/IconButton";
import SendIcon from "@mui/icons-material/Send";
import {createTheme, ThemeProvider} from "@mui/material/styles";

export default function ChatDisplay() {
    const theme = createTheme();

    const initialPromptResponse = <MessageLeft
        message="How can I assist with your search today?"
        timestamp={FunctionUtil.getCurrentTime()}
        photoURL="https://lh3.googleusercontent.com/a-/AOh14Gi4vkKYlfrbJ0QLJTg_DLjcYyyK7fYoWRpz2r4s=s96-c"
        displayName="Viki"
        avatarDisp={true}
    />

    const [messages, setMessages] = useState([]);
    const [promptResponse, setPromptResponse] = useState('');

    if (messages.length < 1) {
        setMessages(messages => [
            ...messages,
            initialPromptResponse
        ]);
    }

    function getAxiosConfiguration() {
        return {
            timeout: 6000,
            signal: AbortSignal.timeout(6000)
        };
    }

    async function onPromptSubmit(e) {
        e.preventDefault();
        let promptElement = document.getElementById('standard-text');
        let prompt = promptElement.value.trim();
        if (prompt !== null && prompt.length > 0) {
            console.log("prompt: " + prompt);
            createPromptMessage(prompt);
            promptElement.value = '';
            await handleChatPrompt(prompt);
            createPromptResponse(promptResponse);
        }
    }
    function createPromptMessage(prompt) {
        const promptMessage = <MessageRight
            message={prompt}
            timestamp={FunctionUtil.getCurrentTime()}
            photoURL="https://lh3.googleusercontent.com/a-/AOh14Gi4vkKYlfrbJ0QLJTg_DLjcYyyK7fYoWRpz2r4s=s96-c"
            displayName="Me"
            avatarDisp={true}
        />;
        setMessages(messages => [
            ...messages,
            promptMessage
        ]);
    }

    function createPromptResponse(response) {
        const promptResponse = <MessageLeft
            message={response}
            timestamp={FunctionUtil.getCurrentTime()}
            photoURL="https://lh3.googleusercontent.com/a-/AOh14Gi4vkKYlfrbJ0QLJTg_DLjcYyyK7fYoWRpz2r4s=s96-c"
            displayName="Viki"
            avatarDisp={true}
        />;
        setMessages(messages => [
            ...messages,
            promptResponse
        ]);
    }

    async function handleChatPrompt(prompt) {
        await axios.get(`/opensquare/api/rag/youtube/chat?prompt=${prompt}`, getAxiosConfiguration())
            .then(response => {
                setPromptResponse(response.data);
                if (response.data !== null) {
                    console.log(response.data)
                    return response;
                }
            })
            .catch(error => {
                console.log(error);
            })
    }

    useEffect(() => {
        console.log(promptResponse);
    }, [promptResponse]);


    return (
        <div style={{ width: "50vw", height: "100vh", display: "flex", alignItems: "center", justifyContent: "center" }}>
            <Paper>
                <Paper id="style-1" sx={{ width: "calc( 100% - 20px )", margin: 10, overflowY: "scroll", height: "calc( 100% - 80px )" }}>
                    {messages.map(message => (
                        <div key={FunctionUtil.getRandomInt(10)}>{message}</div>
                    ))}
                </Paper>
                <ThemeProvider theme={theme}>
                    <form style={{ display: "flex", justifyContent: "center", width: "95%", margin: `${theme.spacing(0)} auto` }}  noValidate autoComplete="off">
                        <TextField style={{ width: "100%" }}
                                   id="standard-text"
                                   label="Chat with Viki"
                        />
                        <Button onClick={e => onPromptSubmit(e)} type="submit" variant="contained" color="primary">
                            <SendIcon />
                        </Button>
                    </form>
                </ThemeProvider>
            </Paper>
        </div>
    );
}