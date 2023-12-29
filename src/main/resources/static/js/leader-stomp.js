let socket;
let stompClient;

let maxRetryCount = 5;

const OPTIONS_PRIVATE_CHANNEL = "/topic/private/options/";
const USERS_PRIVATE_CHANNEL = "/topic/private/users/";
const LEADER_EXIT_NOTIFICATION_PRIVATE_CHANNEL = "/topic/private/leader-exit/";
const USER_EXIT_NOTIFICATION_PRIVATE_CHANNEL = "/topic/private/user-exit/";


function connection() {
    socket = new SockJS("/ws", []);
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError)

    socket.onclose = () => {
        stompClient.send("/app/play.notifyOnLeaderExit", {}, "EXIT");
    }
}

function onConnected() {
    stompClient.subscribe(OPTIONS_PRIVATE_CHANNEL + currentUser.id, onGenerateRound);
    stompClient.subscribe(USERS_PRIVATE_CHANNEL + currentUser.id, onUserEnter);
    stompClient.subscribe(USER_EXIT_NOTIFICATION_PRIVATE_CHANNEL + currentUser.id, onUserExit)
    stompClient.send("/app/play.notifyAboutPresence", {}, JSON.stringify(currentUser));
}

function onError() {
    if (maxRetryCount > 0) {
        maxRetryCount--;
        connection();
    }
    alert("Attempts are finished, try to reconnect");
    window.location.replace("index.html");
}

function onUserExit(payload) {
    const user = JSON.parse(payload.body);
    if (user) {
        const userEls = document.getElementsByClassName("loaded_user");
        for (let userEl of userEls)
            if (userEl.textContent === user.userName) {
                user.isActive = false;
                stompClient.send("/app/play.updateUser", {}, JSON.stringify(user));
                userEl.remove();
                break;
            }
    }
}

function onGenerateRound(payload) {
    removeChildren("option_round")
    const rawOptions = JSON.parse(payload.body);
    if (rawOptions) {
        const options = rawOptions.options;
        const guessNumber = rawOptions.guessNumber;
        const containerOptions = document.getElementsByClassName("options__container_round")[0];
        for (let i = 0; i < options.length; i++) {
            const divEl = document.createElement("div");
            divEl.className = "option_round";
            divEl.textContent = options[i].optionName;
            if (i === guessNumber) {
                divEl.style.backgroundColor = "lightseagreen";
            }

            containerOptions.append(divEl);
        }

        switchBottomOverflowIndicator(containerOptions);
    }
}

function switchBottomOverflowIndicator(containerOptions) {
    const bottomOverflowIndicator = document.getElementById("bottom_overflow_indicator");
    if (containerOptions.scrollHeight > containerOptions.clientHeight) {
        bottomOverflowIndicator.style.display = "block";
    } else {
        bottomOverflowIndicator.style.display = "none";
    }
}

function onUserEnter(payload) {
    const user = JSON.parse(payload.body);
    if (user) {
        const containerEl = document.getElementsByClassName("modal__container_users_list")[0];

        const divEl = document.createElement("div");
        divEl.className = "loaded_user";
        divEl.textContent = user.userName;
        if (user.isActive) {
            divEl.style.backgroundColor = "forestgreen";
        }
        divEl.addEventListener("click", () => {
            if (user.isActive) {
                user.isActive = false;
                divEl.style.backgroundColor = "salmon";
            } else {
                user.isActive = true;
                divEl.style.backgroundColor = "forestgreen"
            }
            stompClient.send("/app/play.updateUser", {}, JSON.stringify(user));
        })


        containerEl.append(divEl);
    }
}

function generateRound() {
    stompClient.send("/app/play.generateRound", {}, JSON.stringify(selectedTheme));
}

const genRoundBtn = document.getElementById("buildRoundBtn");
genRoundBtn.addEventListener("click", generateRound);