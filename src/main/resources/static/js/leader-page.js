let selectedTheme;

function attachElToSelectTheme() {
    const loadedThemes = document.getElementsByClassName("loaded_theme");
    for (let loadedTheme of loadedThemes) {
        loadedTheme.addEventListener("click", selectTheme);
    }
}

function selectTheme(event) {
    const loadedThemes = document.getElementsByClassName("loaded_theme");
    for (let loadedTheme of loadedThemes) {
        loadedTheme.style.backgroundColor = "crimson";
    }

    const selectedThemeName = event.currentTarget.textContent;
    for (let theme of themes) {
        if (theme.themeName === selectedThemeName) {
            selectedTheme = theme;
            break;
        }
    }
    event.currentTarget.style.backgroundColor = "forestgreen";
}

function startSession() {
    const username = document.getElementById("inputLogin").value.trim();
    if (username) {
        const userDTO = {
            userName: username,
            role: "LEADER",
            isActive: true,
        };
        initConnection("/generateSession", userDTO);
    }
}

const modalUsersCloseBtn = document.getElementById("modalContainerUsersCloseBtn");
modalUsersCloseBtn.addEventListener("click", () => {
    document.getElementsByClassName("modal__container_users")[0].style.display = "none";
});
const connectToSessionBtn = document.getElementById("userConnectToSessionBtn");
connectToSessionBtn.addEventListener("click", startSession);
const userManagementDiv = document.querySelector(".management.user");
userManagementDiv.addEventListener("click", () => {
    document.getElementsByClassName("modal__container_users")[0].style.display = "flex";
});