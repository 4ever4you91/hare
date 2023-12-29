const USER_PAGE = "/user-page.html";
const LEADER_PAGE = "/leader-page.html";

const leaderBtn = document.getElementById("leader_enter");
const userBtn = document.getElementById("user_enter");
const sessionInputCloseBtn = document.getElementById("modalContainerSessionInputCloseBtn");
const connectToSession = document.getElementById("sessionInputConnectToSessionBtn");

leaderBtn.addEventListener("click", () => {
    window.location.assign(window.location.origin + LEADER_PAGE);
});

userBtn.addEventListener("click", () => {
    document.getElementsByClassName("modal__container_session_input")[0].style.display = "flex";
})

connectToSession.addEventListener("click", () => {
    const sessionId = document.getElementById("inputSessionId").value.trim();
    if (sessionId) {
        window.location.assign(window.location.origin + USER_PAGE + "?sessionId=" + sessionId)
    }
});

sessionInputCloseBtn.addEventListener("click", () => {
    document.getElementsByClassName("modal__container_session_input")[0].style.display = "none";
});