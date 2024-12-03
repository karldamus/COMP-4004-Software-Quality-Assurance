const apiBaseUrl = "http://localhost:8080";

let lastOutput = "";

let clearScreen = false;

const inputField = document.getElementById("game-input");
const submitButton = document.getElementById("submit");
const outputContainer = document.getElementById("game-output");

const enterButton = document.getElementById("enter");

// let isFirstRun = true;

// if (isFirstRun) {
// 	sendInput("\n");
// 	isFirstRun = false;
// }

async function sendInput(input) {
	const response = await fetch(`${apiBaseUrl}/input`, {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
		},
		body: JSON.stringify(input),
	});

	document.getElementById("game-input").value = "";
	if (!response.ok) {
		console.error("Error sending input: ", error);
	}
}

// async function fetchOutput() {
// 	const response = await fetch(`${apiBaseUrl}/output`);
// 	if (response.ok) {
// 		const data = await response.text();
// 		if (data !== lastOutput) {
// 			document.getElementById("game-output").innerText = data;
// 			lastOutput = data;
// 		}
// 	}
// }

submitButton.addEventListener("click", () => {
	const input = inputField.value.trim();
	if (input) {
		sendInput(input);
	}
});

enterButton.addEventListener("click", () => {
	if (clearScreen) {
		outputContainer.innerHTML = "";
		clearScreen = false;
	}

	sendInput("\n");
});

async function fetchOutputs() {
	try {
		const response = await fetch(`${apiBaseUrl}/output`);
		const outputs = await response.json();

		outputs.forEach(message => {
			const messageElement = document.createElement("p");
			messageElement.innerText = message;
			outputContainer.appendChild(messageElement);

			if (message.includes("clear")) {
				clearScreen = true;
			}
		});

		outputContainer.scrollTop = outputContainer.scrollHeight;
	} catch (error) {
		console.error("Error fetching outputs: ", error);;
	}
}

async function newGame() {
	try {
		const response = await fetch(`${apiBaseUrl}/newGame`);
	} catch (error) {
		console.error(error);
	}
}

setInterval(fetchOutputs, 1000);

// var currentAction = "none";

// var inputButtonPressed = false;


// async function test() {
// 	try {
// 		const response = await fetch(`${apiBaseUrl}/test`);
// 		const data = await response.text();
// 		console.log(data);
// 	} catch (error) {
// 		console.error(error);
// 	}
// }

// async function newGame() {
// 	try {
// 		const response = await fetch(`${apiBaseUrl}/newGame`);
// 		// const data = await response.text();
// 		clearScreen();
// 		addText("Welcome to a Game of Quests!<br><br>");
// 		// addText(data);
// 	} catch (error) {
// 		console.error(error);
// 	}

// 	while (true) {
// 		await test();
// 	}
// }

// async function playerTurn(playerNumber) {
// 	try {
// 		await displayPlayerHand(playerNumber);
// 		await displayPlayerShields(playerNumber);

// 		currentAction = "drawing card";

// 	} catch (error) {
// 		console.error(error);
// 	}
// }

// async function displayPlayerHand(playerNumber) {
// 	const playerHandRequest = await fetch(`${apiBaseUrl}/playerHand?playerNumber=${playerNumber}`);
// 	const playerHandResponse = await playerHandRequest.text();
// 	addText(playerHandResponse);
// }

// async function displayPlayerShields(playerNumber) {
// 	const playerShieldsRequest = await fetch(`${apiBaseUrl}/playerShields?playerNumber=${playerNumber}`);
// 	const playerShieldsResponse = await playerShieldsRequest.text();
// 	addText(playerShieldsResponse);
// }

// var lastOutput = "";

// async function test() {
// 	const output = await fetch(`${apiBaseUrl}/getOutput`);
// 	const outputText = await output.text();
	
// 	if (outputText != lastOutput) {
// 		addText(outputText);
// 		lastOutput = outputText;
// 	}
// }

// async function input() {
// 	const input = document.getElementById("input").value;
// 	const response = await fetch(`${apiBaseUrl}/setInput?input=${input}`);

// 	input.innerHTML = "";
// }


// async function sendInput(input) {
// 	// const input = document.getElementById("input").value;
// 	const response = await fetch(`${apiBaseUrl}/input?input=${input}`);

// 	const data = await response.text();
// 	console.log(data);

// 	input.innerHTML = "";
// }


// // send a simple text message to the server no json
// async function sendMessage() {
// 	const message = document.getElementById("message").value;
// 	const response = await fetch(`${apiBaseUrl}/sendMessage?message=${message}`);

// 	const data = await response.text();
// 	console.log(data);
// }

// async function addText(message) {
// 	const gameOutput = document.getElementById("gameOutput");
// 	const p = document.createElement("p");
// 	p.innerHTML = message;
// 	gameOutput.appendChild(p);
// }

// async function clearScreen() {
// 	const gameOutput = document.getElementById("gameOutput");

// 	gameOutput.innerHTML = "";
// }

