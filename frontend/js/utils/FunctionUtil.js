const getPublishDate = (dateStr) => {
    const date = new Date(Date.parse(dateStr));
    let day = date.getDate();
    let month = getMonthString(date.getMonth());
    let year = date.getFullYear();
    return `${day} ${month}, ${year}`;
};

const getCurrentDate = () => {
    const date = new Date();
    let day = date.getDate();
    let month = getMonthString(date.getMonth());
    let year = date.getFullYear();
    return `as of ${day} ${month}, ${year}`;
}

const getCurrentTime = () => {
    const time = new Date();
    return time.toTimeString().split(" ")[0];
}

const getRandomInt = (max) => {
    return Math.floor(Math.random() * max);
}

function random(mn, mx) {
    return Math.random() * (mx - mn) + mn;
}

function randomGreeting() {
    return greetings[Math.floor(random(1, greetings.length + 1)) - 1];
}

const greetings = [
    "How can I assist with your search today?",
    "Back already?",
    "Welcome back!",
    "What can I do for you?",
    "Miss me already, what's up?",
    "Yo!"
];

const getMonthString = (monthInt) => {
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
};

export default {
    getPublishDate,
    getCurrentDate,
    getMonthString,
    getCurrentTime,
    getRandomInt,
    randomGreeting
};