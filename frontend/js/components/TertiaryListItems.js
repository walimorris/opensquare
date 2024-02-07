import * as React from 'react';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import InsertDriveFileIcon from '@mui/icons-material/InsertDriveFile';
import ListSubheader from '@mui/material/ListSubheader';
import SettingsModal from "./SettingsModal";
import {useEffect, useState} from "react";
import axios from "axios";

export default function TertiaryListItems(props) {

    return (
        <React.Fragment>
            <ListSubheader component="div" inset>
                Settings
            </ListSubheader>
            <ListItemButton>
                <ListItemIcon>
                    <InsertDriveFileIcon />
                </ListItemIcon>
                <ListItemText primary="Documentation" />
            </ListItemButton>
            <SettingsModal userDetails={props.userDetails}/>
        </React.Fragment>
    )
}
