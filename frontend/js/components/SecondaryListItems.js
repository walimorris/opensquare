import * as React from "react";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";
import ListSubheader from "@mui/material/ListSubheader";
import StackedLineChartIcon from "@mui/icons-material/StackedLineChart";
import FilePresentIcon from "@mui/icons-material/FilePresent";

export default function SecondaryListItems(props) {

    const handleTrigger = (e) => {
        const dataAttribute = e.target.textContent;
        const expandAttribute = e.target.id;
        let usableValue = dataAttribute !== '' ? dataAttribute : expandAttribute;

        console.log(usableValue);
        props.handleToggleAll(usableValue);
        e.preventDefault();
    }

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
            <ListItemButton onClick={handleTrigger}>
                <ListItemIcon>
                    <FilePresentIcon />
                </ListItemIcon>
                <ListItemText primary="Reports" />
            </ListItemButton>
        </React.Fragment>
    )
}
