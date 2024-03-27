import React, { useEffect, useState } from "react";
import Paper from '@mui/material/Paper';
import { MessageLeft, MessageRight } from "./ChatDisplayMessage";
import FunctionUtil from "../utils/FunctionUtil";
import axios from "axios";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/IconButton";
import SendIcon from "@mui/icons-material/Send";
import { createTheme, ThemeProvider } from "@mui/material/styles";

export default function ChatDisplay() {
    const theme = createTheme();

    const [isAnchorOpen, setIsAnchorOpen] = useState(true); // State to track if anchor is open
    const [messages, setMessages] = useState([]);
    const [initialPromptRendered, setInitialPromptRendered] = useState(false);

    const CHAT_BOT_DISPLAY = 'chatbox-display';

    /**
     * Each message is serialized into a plain JavaScript object before storing them in localStorage.
     * When retrieving messages from localStorage, each object is deserialized back into a React element
     * before setting them in state.Make sure to adjust the serialization and deserialization logic based
     * on the structure of your MessageLeft and MessageRight components. The key point is to ensure that
     * you're storing and retrieving plain JavaScript objects in localStorage, and then converting them back
     * into React elements when rendering.
     */
    useEffect(() => {
        const storedMessages = JSON.parse(localStorage.getItem('chatMessages'));
        if (storedMessages) {
            // Deserialize each message before setting them in state
            const deserializedMessages = storedMessages.map(message => {
                if (message.type === 'right') {
                    return <MessageRight
                        key={message.key}
                        message={message.message}
                        timestamp={message.timestamp}
                        photoURL={message.photoURL}
                        displayName={message.displayName}
                        avatarDisp={message.avatarDisp}
                    />;
                } else {
                    return <MessageLeft
                        key={message.key}
                        message={message.message}
                        timestamp={message.timestamp}
                        photoURL={message.photoURL}
                        displayName={message.displayName}
                        avatarDisp={message.avatarDisp}
                    />;
                }
            });
            setMessages(deserializedMessages);
            setChatScroll();
        }
    }, []);

    useEffect(() => {
        // Serialize each message before storing them in localStorage
        const serializedMessages = messages.map((message, index) => ({
            type: message.type === MessageRight ? 'right' : 'left',
            key: index,
            message: message.props.message,
            timestamp: message.props.timestamp,
            photoURL: message.props.photoURL,
            displayName: message.props.displayName,
            avatarDisp: message.props.avatarDisp
        }));
        localStorage.setItem('chatMessages', JSON.stringify(serializedMessages));
    }, [messages]);

    useEffect(() => {
        if (isAnchorOpen && !initialPromptRendered) {
            createPromptResponse(FunctionUtil.randomGreeting());
            setInitialPromptRendered(true);
        }
        setChatScroll();
    }, [isAnchorOpen, messages, initialPromptRendered]);

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
            const response = await handleChatPrompt(prompt);
            createPromptResponse(response);
        }
    }

    function createPromptMessage(prompt) {
        const promptMessage = <MessageRight
            message={prompt}
            timestamp={FunctionUtil.getCurrentTime()}
            photoURL="/images/professional_me.jpeg"
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
            photoURL="/images/viki-avatar.jpeg"
            displayName="Viki"
            avatarDisp={true}
        />;
        setMessages(messages => [
            ...messages,
            promptResponse
        ]);
    }

    async function handleChatPrompt(prompt) {
        try {
            const response = await axios.get(`/opensquare/api/rag/youtube/chat?prompt=${prompt}`, getAxiosConfiguration());
            if (response.data !== null) {
                console.log(response.data)
                return response.data;
            }
        } catch (error) {
            console.log(error);
        }
    }

    /**
     * Sets chatbox scroll to bottom when it's opened or a new message is appended.
     * It should be noted that this effects the scrollbar in the ChatDisplayAnchor
     * element, which happens to be the parent element of the overall chatbox element.
     */
    function setChatScroll() {
        const chatboxDisplay = document.getElementById(CHAT_BOT_DISPLAY);
        const chatToggleElement = chatboxDisplay.parentElement;
        chatToggleElement.scrollTop = chatToggleElement.scrollHeight;
    }

    return (
        <div style={{ width: "50vw", height: "100vh", display: "flex", alignItems: "center", justifyContent: "center" }}>
            <Paper id="chatbox" onChange={setChatScroll}>
                <Paper id="style-1" sx={{ width: "calc( 100% - 20px )", margin: 10, overflowY: "scroll", height: "calc( 100% - 80px )" }}>
                    {messages.map((message, index) => (
                        <div key={index}>{message}</div> // Wrap each message in a <div>
                    ))}
                </Paper>
                <ThemeProvider theme={theme}>
                    <form style={{ display: "flex", justifyContent: "center", width: "95%", margin: `${theme.spacing(0)} auto` }} noValidate autoComplete="off">
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
