// games.js - Additional Ice Breaking Games
class IceBreakingGames {
    constructor() {
        this.games = {
            mathRace: {
                name: "Math Race",
                description: "Lomba matematika cepat",
                timeLimit: 60,
                maxPlayers: 5,
                difficulty: ['easy', 'medium', 'hard']
            },
            wordChain: {
                name: "Rantai Kata",
                description: "Sambung kata terakhir",
                timeLimit: 120,
                maxPlayers: 10,
                categories: ['umum', 'pendidikan', 'sains']
            },
            quickQuiz: {
                name: "Quick Quiz",
                description: "Kuis cepat berbagai topik",
                timeLimit: 90,
                maxPlayers: 4,
                categories: ['matematika', 'sains', 'sejarah', 'bahasa']
            },
            memoryMatch: {
                name: "Memory Match",
                description: "Temukan pasangan yang cocok",
                timeLimit: 180,
                maxPlayers: 2,
                difficulty: ['easy', 'medium', 'hard']
            }
        };
        
        this.leaderboard = JSON.parse(localStorage.getItem('gameLeaderboard')) || {};
        this.currentGame = null;
    }

    // Start a game
    startGame(gameType, options = {}) {
        this.currentGame = {
            type: gameType,
            startTime: Date.now(),
            players: [],
            scores: {},
            ...options
        };

        switch(gameType) {
            case 'mathRace':
                return this.startMathRace(options);
            case 'wordChain':
                return this.startWordChain(options);
            case 'quickQuiz':
                return this.startQuickQuiz(options);
            case 'memoryMatch':
                return this.startMemoryMatch(options);
            default:
                throw new Error('Game not found');
        }
    }

    // Math Race Game
    startMathRace(options) {
        const difficulty = options.difficulty || 'medium';
        const problems = this.generateMathProblems(difficulty, 20);
        
        return {
            type: 'mathRace',
            problems,
            timeLimit: this.games.mathRace.timeLimit,
            currentProblem: 0,
            score: 0,
            startTime: Date.now()
        };
    }

    generateMathProblems(difficulty, count) {
        const problems = [];
        const operations = ['+', '-', '×', '÷'];
        
        for (let i = 0; i < count; i++) {
            let num1, num2, operation, answer;
            
            switch(difficulty) {
                case 'easy':
                    num1 = Math.floor(Math.random() * 20) + 1;
                    num2 = Math.floor(Math.random() * 10) + 1;
                    operation = operations[Math.floor(Math.random() * 2)];
                    break;
                case 'medium':
                    num1 = Math.floor(Math.random() * 50) + 10;
                    num2 = Math.floor(Math.random() * 20) + 1;
                    operation = operations[Math.floor(Math.random() * 3)];
                    break;
                case 'hard':
                    num1 = Math.floor(Math.random() * 100) + 50;
                    num2 = Math.floor(Math.random() * 50) + 1;
                    operation = operations[Math.floor(Math.random() * 4)];
                    break;
            }
            
            // Calculate answer
            switch(operation) {
                case '+': answer = num1 + num2; break;
                case '-': answer = num1 - num2; break;
                case '×': answer = num1 * num2; break;
                case '÷': 
                    answer = (num1 / num2).toFixed(2);
                    // Make sure division is clean
                    num1 = num2 * Math.floor(Math.random() * 10 + 1);
                    answer = num1 / num2;
                    break;
            }
            
            // Generate wrong answers
            const wrongAnswers = this.generateWrongAnswers(answer, 3);
            
            problems.push({
                question: `${num1} ${operation} ${num2} = ?`,
                correctAnswer: answer,
                answers: this.shuffleArray([answer, ...wrongAnswers]),
                points: difficulty === 'easy' ? 10 : difficulty === 'medium' ? 15 : 20
            });
        }
        
        return problems;
    }

    generateWrongAnswers(correct, count) {
        const wrong = [];
        const used = new Set();
        
        while (wrong.length < count) {
            let variation;
            if (typeof correct === 'number') {
                const offset = Math.floor(Math.random() * 10) + 1;
                variation = Math.random() > 0.5 ? correct + offset : correct - offset;
                variation = Math.round(variation);
            }
            
            if (variation !== correct && !used.has(variation)) {
                wrong.push(variation);
                used.add(variation);
            }
        }
        
        return wrong;
    }

    // Word Chain Game
    startWordChain(options) {
        const category = options.category || 'umum';
        const startWord = this.getStartWord(category);
        
        return {
            type: 'wordChain',
            category,
            currentWord: startWord,
            chain: [startWord],
            usedWords: new Set([startWord]),
            players: [],
            scores: {},
            timeLimit: this.games.wordChain.timeLimit
        };
    }

    getStartWord(category) {
        const words = {
            umum: ['SEKOLAH', 'PENDIDIKAN', 'BELAJAR', 'GURU', 'SISWA'],
            pendidikan: ['MATEMATIKA', 'FISIKA', 'KIMIA', 'BIOLOGI', 'SEJARAH'],
            sains: ['ELEKTRON', 'PROTON', 'MOLEKUL', 'SEL', 'DNA']
        };
        
        const categoryWords = words[category] || words.umum;
        return categoryWords[Math.floor(Math.random() * categoryWords.length)];
    }

    isValidWord(currentWord, newWord) {
        if (!newWord || newWord.length < 2) return false;
        
        const lastLetter = currentWord[currentWord.length - 1].toUpperCase();
        const firstLetter = newWord[0].toUpperCase();
        
        return lastLetter === firstLetter;
    }

    // Quick Quiz Game
    startQuickQuiz(options) {
        const category = options.category || 'matematika';
        const questions = this.getQuizQuestions(category, 10);
        
        return {
            type: 'quickQuiz',
            category,
            questions,
            currentQuestion: 0,
            scores: {},
            timeLimit: this.games.quickQuiz.timeLimit
        };
    }

    getQuizQuestions(category, count) {
        // This would typically come from a database
        const questionBank = {
            matematika: [
                {
                    question: "Berapakah hasil dari 15²?",
                    answers: ["225", "250", "275", "200"],
                    correct: 0,
                    explanation: "15 × 15 = 225"
                },
                {
                    question: "Apa nilai dari π (pi) hingga 2 desimal?",
                    answers: ["3.14", "3.15", "3.16", "3.17"],
                    correct: 0,
                    explanation: "π ≈ 3.14159"
                }
            ],
            sains: [
                {
                    question: "Planet terdekat dari matahari adalah?",
                    answers: ["Merkurius", "Venus", "Bumi", "Mars"],
                    correct: 0,
                    explanation: "Merkurius adalah planet terdekat dari matahari"
                }
            ]
        };
        
        return questionBank[category] || questionBank.matematika;
    }

    // Memory Match Game
    startMemoryMatch(options) {
        const difficulty = options.difficulty || 'medium';
        const gridSize = difficulty === 'easy' ? 4 : difficulty === 'medium' ? 6 : 8;
        const cards = this.generateMemoryCards(gridSize);
        
        return {
            type: 'memoryMatch',
            difficulty,
            gridSize,
            cards,
            flipped: [],
            matched: [],
            score: 0,
            moves: 0,
            timeLimit: this.games.memoryMatch.timeLimit
        };
    }

    generateMemoryCards(gridSize) {
        const totalCards = (gridSize * gridSize) / 2;
        const symbols = ['🍎', '🍌', '🍒', '🍇', '🍊', '🍑', '🍓', '🥭', '🍍', '🥝'];
        
        let selectedSymbols = symbols.slice(0, totalCards);
        selectedSymbols = [...selectedSymbols, ...selectedSymbols];
        
        return this.shuffleArray(selectedSymbols.map((symbol, index) => ({
            id: index,
            symbol,
            flipped: false,
            matched: false
        })));
    }

    // Utility functions
    shuffleArray(array) {
        for (let i = array.length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            [array[i], array[j]] = [array[j], array[i]];
        }
        return array;
    }

    // Leaderboard management
    updateLeaderboard(gameType, playerName, score) {
        if (!this.leaderboard[gameType]) {
            this.leaderboard[gameType] = [];
        }
        
        this.leaderboard[gameType].push({
            player: playerName,
            score: score,
            date: new Date().toISOString()
        });
        
        // Sort by score descending
        this.leaderboard[gameType].sort((a, b) => b.score - a.score);
        
        // Keep only top 10
        this.leaderboard[gameType] = this.leaderboard[gameType].slice(0, 10);
        
        // Save to localStorage
        localStorage.setItem('gameLeaderboard', JSON.stringify(this.leaderboard));
    }

    getLeaderboard(gameType) {
        return this.leaderboard[gameType] || [];
    }

    // Calculate remaining time
    getRemainingTime(startTime, timeLimit) {
        const elapsed = (Date.now() - startTime) / 1000;
        return Math.max(0, timeLimit - elapsed);
    }
}

// Game UI Manager
class GameUIManager {
    constructor() {
        this.game = new IceBreakingGames();
        this.currentGameState = null;
        this.timerInterval = null;
    }

    showGameSelection() {
        return `
            <div class="row g-3">
                ${Object.entries(this.game.games).map(([key, game]) => `
                    <div class="col-md-6">
                        <div class="game-selection-card" onclick="gameUI.selectGame('${key}')">
                            <div class="text-center p-4">
                                <i class="fas fa-${this.getGameIcon(key)} fa-3x mb-3"></i>
                                <h4>${game.name}</h4>
                                <p class="text-muted">${game.description}</p>
                                <div class="game-info">
                                    <span class="badge bg-primary">
                                        <i class="fas fa-clock me-1"></i> ${game.timeLimit}s
                                    </span>
                                    <span class="badge bg-success ms-2">
                                        <i class="fas fa-users me-1"></i> ${game.maxPlayers}
                                    </span>
                                </div>
                            </div>
                        </div>
                    </div>
                `).join('')}
            </div>
        `;
    }

    getGameIcon(gameType) {
        const icons = {
            mathRace: 'calculator',
            wordChain: 'link',
            quickQuiz: 'question-circle',
            memoryMatch: 'brain'
        };
        return icons[gameType] || 'gamepad';
    }

    selectGame(gameType) {
        this.currentGameState = this.game.startGame(gameType);
        this.startTimer();
        this.renderGame();
    }

    startTimer() {
        if (this.timerInterval) clearInterval(this.timerInterval);
        
        this.timerInterval = setInterval(() => {
            const remaining = this.game.getRemainingTime(
                this.currentGameState.startTime,
                this.currentGameState.timeLimit
            );
            
            if (remaining <= 0) {
                this.endGame();
            } else {
                this.updateTimerDisplay(remaining);
            }
        }, 1000);
    }

    updateTimerDisplay(seconds) {
        const timerElement = document.getElementById('gameTimer');
        if (timerElement) {
            timerElement.textContent = Math.ceil(seconds);
            
            // Update progress bar
            const progress = (seconds / this.currentGameState.timeLimit) * 100;
            const progressBar = document.getElementById('timerProgress');
            if (progressBar) {
                progressBar.style.width = `${progress}%`;
                
                // Change color based on time
                if (progress < 30) {
                    progressBar.className = 'progress-bar bg-danger';
                } else if (progress < 60) {
                    progressBar.className = 'progress-bar bg-warning';
                } else {
                    progressBar.className = 'progress-bar bg-success';
                }
            }
        }
    }

    renderGame() {
        const container = document.getElementById('gameContainer');
        if (!container) return;

        switch(this.currentGameState.type) {
            case 'mathRace':
                container.innerHTML = this.renderMathRace();
                break;
            case 'wordChain':
                container.innerHTML = this.renderWordChain();
                break;
            case 'quickQuiz':
                container.innerHTML = this.renderQuickQuiz();
                break;
            case 'memoryMatch':
                container.innerHTML = this.renderMemoryMatch();
                break;
        }
    }

    renderMathRace() {
        const problem = this.currentGameState.problems[this.currentGameState.currentProblem];
        
        return `
            <div class="game-container">
                <div class="game-header text-center mb-4">
                    <h2>Math Race</h2>
                    <div class="d-flex justify-content-between align-items-center">
                        <div class="score">Score: ${this.currentGameState.score}</div>
                        <div class="problem-count">
                            ${this.currentGameState.currentProblem + 1}/${this.currentGameState.problems.length}
                        </div>
                    </div>
                </div>
                
                <div class="problem-container text-center mb-4">
                    <div class="problem-display p-4">
                        <h1 class="display-1">${problem.question}</h1>
                    </div>
                </div>
                
                <div class="answers-container">
                    <div class="row g-3">
                        ${problem.answers.map((answer, index) => `
                            <div class="col-md-6">
                                <button class="btn btn-primary btn-lg w-100 p-3" 
                                        onclick="gameUI.checkMathAnswer(${index})">
                                    ${answer}
                                </button>
                            </div>
                        `).join('')}
                    </div>
                </div>
                
                <div class="timer-container mt-4">
                    <div class="progress" style="height: 10px;">
                        <div class="progress-bar" id="timerProgress"></div>
                    </div>
                    <div class="text-center mt-2">
                        <small>Time: <span id="gameTimer">${this.currentGameState.timeLimit}</span>s</small>
                    </div>
                </div>
            </div>
        `;
    }

    checkMathAnswer(answerIndex) {
        const problem = this.currentGameState.problems[this.currentGameState.currentProblem];
        const isCorrect = problem.answers[answerIndex] === problem.correctAnswer;
        
        if (isCorrect) {
            this.currentGameState.score += problem.points;
            Swal.fire({
                icon: 'success',
                title: 'Correct!',
                text: `+${problem.points} points`,
                timer: 1000
            });
        } else {
            Swal.fire({
                icon: 'error',
                title: 'Wrong!',
                text: `Correct answer: ${problem.correctAnswer}`,
                timer: 1500
            });
        }
        
        // Next problem or end game
        this.currentGameState.currentProblem++;
        if (this.currentGameState.currentProblem >= this.currentGameState.problems.length) {
            this.endGame();
        } else {
            this.renderGame();
        }
    }

    endGame() {
        clearInterval(this.timerInterval);
        
        Swal.fire({
            title: 'Game Over!',
            html: `
                <h3>Final Score: ${this.currentGameState.score}</h3>
                <p>Great job!</p>
                ${this.currentGameState.type === 'mathRace' ? 
                  `<p>Problems solved: ${this.currentGameState.currentProblem}/${this.currentGameState.problems.length}</p>` : ''}
            `,
            icon: 'info',
            confirmButtonText: 'Play Again',
            showCancelButton: true,
            cancelButtonText: 'Back to Menu'
        }).then((result) => {
            if (result.isConfirmed) {
                this.selectGame(this.currentGameState.type);
            } else {
                this.showGameSelection();
            }
        });
        
        // Update leaderboard
        if (this.currentGameState.score > 0) {
            this.game.updateLeaderboard(this.currentGameState.type, 'Player', this.currentGameState.score);
        }
    }
}

// Initialize game UI
const gameUI = new GameUIManager();

// Export for use in HTML
window.gameUI = gameUI;
window.IceBreakingGames = IceBreakingGames;