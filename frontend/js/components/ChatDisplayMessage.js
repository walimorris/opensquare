import React from "react";
import Avatar from '@mui/material/Avatar';
import { deepOrange } from "@mui/material/colors";
import { createTheme, ThemeProvider } from '@mui/material/styles';

const theme = createTheme();

export const MessageLeft = (props) => {
    const message = props.message ? props.message : "no message";
    const timestamp = props.timestamp ? props.timestamp : "";
    const photoURL = props.photoURL ? props.photoURL : "dummy.js";
    const displayName = props.displayName ? props.displayName : "Viki";
    return (
        <>
            <ThemeProvider theme={theme}>
                <div style={{ display: "flex" }}>
                    <Avatar style={{ color: theme.palette.getContrastText(deepOrange[500]),
                        backgroundColor: deepOrange[500],
                        width: theme.spacing(4),
                        height: theme.spacing(4)}}
                        alt={displayName}
                        src={photoURL}
                    ></Avatar>
                    <div>
                        <div style={{ marginLeft: "20px" }}>{displayName}</div>
                        <div style={{ position: "relative", marginLeft: "20px", marginBottom: "10px", padding: "10px",
                            backgroundColor: "#A8DDFD", width: "60%", textAlign: "left", font: "400 .9em 'Open Sans', sans-serif",
                            border: "1px solid #97C6E3", borderRadius: "10px" }}>
                            <div>
                                <p style={{ padding: 0, margin: 0}}>{message}</p>
                            </div>
                            <div style={{ position: "absolute",
                                fontSize: ".85em",
                                fontWeight: "300",
                                marginTop: "10px",
                                bottom: "-3px",
                                right: "5px" }}>{timestamp}</div>
                        </div>
                    </div>
                </div>
            </ThemeProvider>
        </>
    );
};

export const MessageRight = (props) => {
    const message = props.message ? props.message : "no message";
    const timestamp = props.timestamp ? props.timestamp : "";
    const photoURL = props.photoURL ? props.photoURL : "/images/professional_me.jpeg";
    const displayName = props.displayName ? props.displayName : "Me";
    return (
        <>
            <ThemeProvider theme={theme}>
                <div style={{ display: "flex", justifyContent: "flex-end" }}>
                    <Avatar style={{ color: theme.palette.getContrastText(deepOrange[500]),
                        backgroundColor: deepOrange[500],
                        width: theme.spacing(4),
                        height: theme.spacing(4)}}
                            alt={displayName}
                            src={photoURL}
                    ></Avatar>
                    <div style={{ marginLeft: "20px" }}>{displayName}</div>
                    <div style={{ position: "relative", marginRight: "20px", marginBottom: "10px", padding: "10px", backgroundColor: "#f8e896",
                        width: "60%", textAlign: "left", font: "400 .9em 'Open Sans', sans-serif", border: "1px solid #dfd087",
                        borderRadius: "10px" }}>
                        <p style={{ padding: 0, margin: 0 }}>{message}</p>
                        <div style={{ position: "absolute",
                            fontSize: ".85em",
                            fontWeight: "300",
                            marginTop: "10px",
                            bottom: "-3px",
                            right: "5px" }}>{timestamp}</div>
                    </div>
                </div>
            </ThemeProvider>
        </>
    );
};