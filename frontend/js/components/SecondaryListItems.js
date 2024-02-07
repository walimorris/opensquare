import * as React from "react";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";
import ListSubheader from "@mui/material/ListSubheader";
import StackedLineChartIcon from "@mui/icons-material/StackedLineChart";
import FilePresentIcon from "@mui/icons-material/FilePresent";

export default function SecondaryListItems() {
    return (
        <React.Fragment>
            <ListSubheader component="div" inset>
                Saved reports
            </ListSubheader>
            <ListItemButton>
                <ListItemIcon>
                    <StackedLineChartIcon />
                </ListItemIcon>
                <ListItemText primary="Graphs" />
            </ListItemButton>
            <ListItemButton>
                <ListItemIcon>
                    <FilePresentIcon />
                </ListItemIcon>
                <ListItemText primary="Files" />
            </ListItemButton>
        </React.Fragment>
    )
}
