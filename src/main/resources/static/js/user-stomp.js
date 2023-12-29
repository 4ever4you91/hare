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
        stompClient.send("/app/play.exitUser", {}, "EXIT");
    }
}

function onConnected() {
    stompClient.subscribe(OPTIONS_PRIVATE_CHANNEL + currentUser.id, onGenerateRound);
    stompClient.subscribe(LEADER_EXIT_NOTIFICATION_PRIVATE_CHANNEL + currentUser.id, onLeaderExit);
    stompClient.send("/app/play.exchangeUserInfo", {}, JSON.stringify(currentUser));
}

function onError() {
    if (maxRetryCount > 0) {
        maxRetryCount--;
        connection();
    }
    alert("Attempts are finished, try to reconnect");
    window.location.replace("index.html");
}

function onLeaderExit(payload) {
    alert("Leader exited, session finished");
    window.location.replace("index.html");
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

        switchBottomOverflowIndicator();
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