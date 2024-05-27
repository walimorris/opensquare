import * as React from "react";
import Grid from "@mui/material/Grid";
import Paper from "@mui/material/Paper";
import { styled } from '@mui/material/styles';
import ReportsCard from "./ReportsCard";

export default function Reports() {

    const Item = styled(Paper)(({ theme }) => ({
        backgroundColor: theme.palette.mode === 'dark' ? '#1A2027' : '#f3f3f3',
        ...theme.typography.body2,
        padding: theme.spacing(5),
        color: theme.palette.text.secondary,
        textAlign: 'left',
        maxWidth: '100%',
        palette:{
            background: {
                tertiary: '#bfbfbf'
            },
            text: {
                primary: '#fff'
            }
        }
    }));

    function getAxiosConfiguration() {
        return {
            timeout: 6000,
            signal: AbortSignal.timeout(6000)
        };
    }

    return (
        <React.Fragment>
            <Grid item xs={12} justifyContent='center' alignItems='center'>
                <ReportsCard />
            </Grid>
        </React.Fragment>
    );
}