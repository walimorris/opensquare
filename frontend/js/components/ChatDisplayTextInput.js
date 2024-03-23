import React, {useEffect, useState} from 'react'
import TextField from '@mui/material/TextField';
import SendIcon from '@mui/icons-material/Send';
import Button from '@mui/material/IconButton';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import axios from "axios";
import InputBase from "@mui/material/InputBase";
import FunctionUtil from "../utils/FunctionUtil";
import {MessageLeft, MessageRight} from "./ChatDisplayMessage";

const theme = createTheme();

export const ChatDisplayTextInput = (props) => {
    const [promptResponse, setPromptResponse] = useState('');

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
        props.messages.push(promptMessage);
        console.log(props.messages.length)
        props.callBack(props.messages);
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
        console.log(promptResponse.data);
    }, [promptResponse.data]);

    return (
        <>
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
        </>
    )
}