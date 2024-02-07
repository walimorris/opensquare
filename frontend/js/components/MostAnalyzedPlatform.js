import * as React from 'react';
import Link from '@mui/material/Link';
import Typography from '@mui/material/Typography';
import Title from './Title';
import '../../css/platform-options.css';

const images = ['/images/youtube.png', '/images/vkontakte.png', '/images/twitter.png'];
const YOUTUBE = 'youtube';
const TWITTER = 'twitter';
const VKONTAKTE = 'vkontakte';
const PLATFORM_ANALYSIS_TRACKER_PROPERTY = 'platformAnalysisTracker';

function preventDefault(event) {
    event.preventDefault();
}

function getCurrentDate() {
    const date = new Date();
    let day = date.getDate();
    let month = getMonthString(date.getMonth());
    let year = date.getFullYear();
    return `as of ${day} ${month}, ${year}`;
}

function getMonthString(monthInt) {
    switch (monthInt) {
        case 0:
            return 'January';
        case 1:
            return 'February';
        case 2:
            return 'March';
        case 3:
            return 'April';
        case 4:
            return 'May';
        case 5:
            return 'June';
        case 6:
            return 'July';
        case 7:
            return 'August';
        case 8:
            return 'September';
        case 9:
            return 'October';
        case 10:
            return 'November';
        default:
            return 'December';
    }
}

/**
 * Gets Users most analyzed platform. Using the PlatformAnalysisTracker object.
 * Values are feed to an iterated map that returns the platform with the greatest
 * integer value.
 *
 * @param platformAnalysisTracker the PlatformAnalysisTracker property from UserDetails
 * @return String image path to most analyzed platform
 */
function getMostAnalyzedPlatformImage(platformAnalysisTracker) {
    let maxPlatform;
    if (platformAnalysisTracker !== null && platformAnalysisTracker !== undefined) {
        let maxValue = 0;
        let platformMap = new Map([
            [YOUTUBE, platformAnalysisTracker[YOUTUBE]],
            [TWITTER, platformAnalysisTracker[TWITTER]],
            [VKONTAKTE, platformAnalysisTracker[VKONTAKTE]]
        ]);
        /**
         * If two keys have the same value, this process keeps the first
         * greatest value until a value greater is seen.
         */
        for (let [key, value] of platformMap) {
            if (value > maxValue) {
                maxValue = value;
                maxPlatform = key;
            }
        }
        if (maxPlatform !== undefined) {
            return getImagePath(maxPlatform);
        }
    }
    // default image
    return images[0];
}

/**
 * Gets Image path from constant list of images if the image path contains
 * the platform signature.
 *
 * @param platform name of platform signature - i.e. youtube
 * @return String image path
 */
function getImagePath(platform) {
    let resultPath;
    images.forEach((imagePath) => {
        if (imagePath.includes(platform)) {
            resultPath = imagePath;
        }
    });
    return resultPath;
}

export default function MostAnalyzedPlatform({userDetails}) {
    return (
        <React.Fragment>
            <Title>Most Analyzed Platform</Title>
            <img className='platformImage'
                 src={getMostAnalyzedPlatformImage(userDetails[PLATFORM_ANALYSIS_TRACKER_PROPERTY])}
                 alt='platform-image'/>
            <Typography color="text.secondary" sx={{ flex: 1 }}>
                {getCurrentDate()}
            </Typography>
            <div>
                <Link color="primary" href="#" onClick={preventDefault}>
                    View past results
                </Link>
            </div>
        </React.Fragment>
    );
}
