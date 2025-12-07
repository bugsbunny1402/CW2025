## GitHub Repository  
[CW2025 – GitHub Link](https://github.com/bugsbunny1402/CW2025)

## Compilations Instructions 
**Prerequisites**
Java JDK: Version 17 or higher
Maven: Version 3.6 or higher
JavaFX: Version 21.0.6 (automatically downloaded by Maven)

1. Open the project folder in IntelliJ
2. Maven will automatically detect pom.xml and download dependencies.
3. To Run: Right-click on Launcher.java → Run 'Launcher.main()'
OR Right-click on Main.java → Run 'Main.main()'
4. To Run Tests: Right-click on src/test/java folder → Run 'All Tests'

## Implemented and Working Features
## Core Gameplay Features

### 1. Tetris Piece Movement  
- Full implementation of all seven Tetris pieces (I, O, T, S, Z, J, L).  
- **Left/Right Movement:** Arrow keys move pieces horizontally with proper collision detection.  
- **Soft Drop:** Down arrow drops piece by one row, awarding **1 point per row**.  
- **Rotation:** Up arrow rotates pieces counter-clockwise, including wall-kick prevention.  
- **Auto-Drop:** Pieces automatically fall based on the current game level and speed.  

### 2. Hard Drop Mechanism  
- Pressing **Space** instantly drops the active piece to its lowest valid position.  
- Awards **2 points per row traveled**.  
- Immediately locks the piece and spawns the next one.

### 3. Hold Piece System  
- Press **H** to store the current piece or swap with the held piece.  
- First use stores the active piece; subsequent uses swap.  
- Limited to **one hold per piece drop** to prevent abuse.  
- Hold preview panel shows the stored piece.

### 4. Ghost Piece Indicator  
- Displays a transparent “ghost” version of the current piece.  
- Shows where the piece will land if hard-dropped.  
- Helps players plan strategic placements.

### 5. Line Clearing  
- Detects and clears complete horizontal lines.  
- Supports single, double, triple, and Tetris clears.  
- Rows above shift down to fill gaps.  
- Includes animated highlight and fade effects for cleared lines.

### 6. Combo System  
- Combo counter increments with each consecutive line clear.  
- Bonus = **base score × combo count**.  
- Visual combo notifications appear during consecutive clears.  
- Combo resets when a non-clearing move occurs.


### 7. Scoring System (Strategy Pattern)  
- Scoring implemented using interchangeable strategies:  
  - **StandardScoringStrategy:** `50 × lines²`  
  - **AggressiveScoringStrategy:** `100 × lines²`  
- Supports soft drop, hard drop, and line-clear scoring.  
- Score UI updates in real-time.

### 8. Progressive Difficulty  
- Level increases every **10 lines cleared**.  
- Falling speed increases with each level.  
- Level displayed on-screen at all times.

### 9. Next Piece Preview  
- Dedicated preview panel shows the next piece.  
- Helps players plan ahead for upcoming shapes.

## Game State Management
### 10. Pause Functionality  
- Press **P** to pause or resume the game.  
- Freezes piece movement and auto-drop timer.  
- Displays a pause indicator on the UI.  
- All gameplay inputs disabled while paused.


### 11. New Game System  
- Press **N** to instantly start a new game.  
- Resets board, score, level, combo counter, and held piece.  
- Restarts falling timer correctly (timeline restart bug fixed).  

### 12. Game Over Detection  
- Game detects when a new piece cannot spawn.  
- Displays centered **GAME OVER** message with fade-in.  
- Stops timers and movement.  
- Final score remains visible.

### 13. High Score Persistence  
- Best score saved between game sessions.  
- Uses `highscore.dat` with `DataInputStream/DataOutputStream`.  
- Automatically saves new high scores when the game ends.

## User Interface Features
### 14. Start Menu  
- Professional main menu with:  
  - **Start Game**  
  - **Exit Game**  
- Clean layout with neon styling.

### 15. Visual Animations  
- Flashing animation for cleared lines.  
- Fade-out effect prior to row removal.  
- Smooth color transitions across UI components.

### 16. Background Graphics  
- Custom background image integrated across the entire game.  
- Styled to maintain visual clarity and readability.

### 17. Real-Time Display Updates  
Dynamic, live-updating UI for:  
- Score  
- High score  
- Level  
- Combo counter  
- Next piece  
- Hold piece  

## Audio Features

### 18. Sound Effects  
- Line clear sound.  
- Game over sound.  
- System includes support for movement/rotation/drop sounds (future use).

### 19. Background Music  
- Looping background track during gameplay.  
- Volume adjustment and mute functionality supported.

## Software Engineering Features
### 20. MVC Architecture  
- **Model:** Game logic (Board, Score, Brick hierarchy).  
- **View:** Rendering and UI (GuiController, Renderer).  
- **Controller:** Game flow coordination (GameController).  
- Clear separation enhances maintainability.

### 21. Design Patterns  
- **Strategy Pattern:** Scoring algorithms are interchangeable.  
- **Interface Segregation:** Brick, Board, BrickGenerator, InputEventListener interfaces.  
- **Dependency Injection:** Controllers receive dependencies rather than creating them.

### 22. Comprehensive JavaDoc  
- Full documentation for all classes and methods.  
- Includes `@param`, `@return`, `@throws`, and `@since` tags.  
- Clear explanations of responsibilities and design decisions.

### 23. Unit Testing  
- **48 unit tests across 8 test suites**, including:  
  - ScoreTest  
  - MatrixOperationsTest  
  - ClearRowTest  
  - BrickRotatorTest  
  - ViewDataTest  
  - HighScoreManagerTest  
  - SimpleBoardTest  
  - ScoringStrategyTest  
- Provides strong regression protection and reliability.

### 24. Package Organization  
**8 fully organized packages:**  

## Implemented but Not Working Properly
*Not currently* - All features are working after bug fixes.
During development, several features required debugging :
- Game Over display initially had visibility issues due to CSS colour conflicts. (fixed)
- New Game function didn't restart the timeline properly  causing bricks to freeze (reverted to working version)
- Background image disappeared after refactoring due to CSS path issues (fixed)
- Some package imports broke after reorganisation (fixed)

## Features Not Implemented
- **Multiplayer mode** - The game only supports single-player.
- **Difficulty selection menu** - Players cannot choose starting difficulty level.
- **Leaderboard** - Only stores one high score, not a full leaderboard

## New Java Classes
## Model Package – Data Transfer Objects

### 1. ClearRow  
**Location:** `src/main/java/com/comp2042/model/ClearRow.java`  

**Purpose:**  
Encapsulates the results of a line-clearing operation.  
Stores:  
- Number of lines removed  
- Updated board matrix  
- Score bonus awarded  
- Row indices that were cleared (used for animations)  

Immutable design reduces coupling between board logic, scoring, and animation systems.

### 2. ViewData  
**Location:** `src/main/java/com/comp2042/model/ViewData.java`  

**Purpose:**  
Data transfer object containing all information required to render a game frame, including:  
- Current brick shape & position  
- Next brick preview  
- Ghost piece position  
- Held brick data  

Uses **defensive copying** to prevent accidental modification of internal game state.

### 3. DownData  
**Location:** `src/main/java/com/comp2042/model/DownData.java`  

**Purpose:**  
Combines **ViewData** with **ClearRow** results during downward movement events.  
Used by soft drop and hard drop operations to return updated state and any row-clearing information in one structured response.

### 4. BrickRotator  
**Location:** `src/main/java/com/comp2042/model/BrickRotator.java`  

**Purpose:**  
Manages the rotation state of the active brick.  
Handles:  
- Active rotation index (0–3)  
- Returning current rotation matrix  
- Computing next rotation matrix  

Separates rotation from board logic, improving modularity.

### 5. NextShapeInfo  
**Location:** `src/main/java/com/comp2042/model/NextShapeInfo.java`  

**Purpose:**  
Contains information about a brick’s next rotation state.  
Used by collision detection to validate rotations before applying them, preventing invalid overlapping rotations.


## Scoring Strategy Package

### 6. ScoringStrategy  
**Location:** `src/main/java/com/comp2042/model/scoring/ScoringStrategy.java`  

**Purpose:**  
Interface implementing the **Strategy Pattern** for scoring.  
Defines methods for:  
- Calculating line clear scores  
- Soft drop scoring  
- Hard drop scoring  
Enables interchangeable scoring algorithms.

### 7. StandardScoringStrategy  
**Location:** `src/main/java/com/comp2042/model/scoring/StandardScoringStrategy.java`  

**Purpose:**  
Default scoring algorithm using classic Tetris scoring:  
- `50 × (lines cleared)²`  
- + combo bonuses  
- + soft drop (1 per row)  
- + hard drop (2 per row)

### 8. AggressiveScoringStrategy  
**Location:** `src/main/java/com/comp2042/model/scoring/AggressiveScoringStrategy.java`  

**Purpose:**  
High-reward scoring variant:  
- `100 × (lines cleared)²`  
- Stronger combo multipliers  
- Soft drop (2 per row)  
- Hard drop (5 per row)  
- Demonstrates the flexibility of the Strategy Pattern.
- 
## UI Package

### 9. AnimationManager  
**Location:** `src/main/java/com/comp2042/ui/AnimationManager.java`  

**Purpose:**  
Dedicated class for all line-clearing animations.  
Handles:  
- Flashing and blink effects  
- Fade-in/fade-out transitions  
- Color flashes and highlight animations  

Extracted from `GuiController` to follow **Single Responsibility Principle**.

### 10. GameOverPanel  
**Location:** `src/main/java/com/comp2042/ui/GameOverPanel.java`  

**Purpose:**  
Custom JavaFX `BorderPane` overlay for the **GAME OVER** message.  
Centralizes game-over styling and logic for better reusability and maintainability.

### 11. Renderer  
**Location:** `src/main/java/com/comp2042/ui/Renderer.java`  

**Purpose:**  
Handles all board and piece rendering:  
- Draws static board matrix  
- Renders active falling bricks  
- Paints ghost piece overlays  
- Updates GridPane UI elements efficiently

### 12. NotificationPanel  
**Location:** `src/main/java/com/comp2042/ui/NotificationPanel.java`  

**Purpose:**  
Displays temporary score popups and combo notifications.  
Provides visual feedback for:  
- Soft drop points  
- Hard drop points  
- Line-clear bonuses  
- Combo increases  

## Events Package

### 13. InputEventListener  
**Location:** `src/main/java/com/comp2042/events/InputEventListener.java`  

**Purpose:**  
Interface defining how user inputs are processed.  
Decouples input handling from JavaFX-specific event types, allowing the controller to remain UI-agnostic.


### 14. MoveEvent  
**Location:** `src/main/java/com/comp2042/events/MoveEvent.java`  

**Purpose:**  
Encapsulates user or timer input events.  
Includes:  
- Event type (LEFT, RIGHT, DOWN, ROTATE, HARD_DROP, HOLD)  
- Event source (USER_INPUT vs TIMER)  

Enables the game to distinguish between player actions and automatic gravity.

### 15. EventType  
**Location:** `src/main/java/com/comp2042/events/EventType.java`  

**Purpose:**  
Enumeration of all action types.  
Ensures type safety and cleaner event processing throughout the engine.

### 16. EventSource  
**Location:** `src/main/java/com/comp2042/events/EventSource.java`  

**Purpose:**  
Enumeration representing whether a move is triggered by the **user** or by the **game timer**, enabling different logic or scoring rules.

## Utility Package

### 17. HighScoreManager  
**Location:** `src/main/java/com/comp2042/util/HighScoreManager.java`  

**Purpose:**  
Manages loading and saving the player's high score using file I/O.  
Stores scores in `highscore.dat` in the user directory.  
Ensures scores persist across game sessions through simple static utility methods.

## Modified Java Classes
## Controller Package

### 1. GameController  
**Location:** `src/main/java/com/comp2042/controller/GameController.java`

**Changes Made:**  
- Moved from `com.comp2042` → `com.comp2042.controller`.  
- Implements **InputEventListener** to improve separation of concerns.  
- Updated to use new DTOs: **ViewData**, **DownData**, **ClearRow**.  
- Integrated full **combo system** (increment/reset logic).  
- Updated to work with **MoveEvent** instead of boolean pause/game-over flags.  
- Connected with **AnimationManager** for line-clearing animations.  
- Added comprehensive JavaDoc across all public methods.

**Reasoning:**  
Refactoring improves adherence to **SOLID principles**, especially Single Responsibility & Interface Segregation.  
Controller is now cleaner, more testable, and decoupled from the UI layer.

### 2. GuiController  
**Location:** `src/main/java/com/comp2042/controller/GuiController.java`

**Changes Made:**  
- Moved from `com.comp2042` → `com.comp2042.controller`.  
- Extracted animation logic into the new **AnimationManager** class.  
- Updated to use **ViewData** for rendering instead of direct board access.  
- Added methods for combo indicators and score popups.  
- Integrated **Renderer** for board drawing.  
- Added full JavaDoc coverage.

**Reasoning:**  
Class complexity reduced significantly.  
GuiController now focuses solely on UI coordination, improving maintainability and readability.

## Model Package
### 3. Board  
**Location:** `src/main/java/com/comp2042/model/Board.java`

**Changes Made:**  
- Added `getCurrentLevel()` to interface.  
- Added `swapHoldBrick()` to interface.  
- Improved all JavaDoc descriptions.

**Reasoning:**  
Eliminates the need for `instanceof` casting in GameController.  
Follows **Liskov Substitution Principle**, enabling any Board implementation to be used interchangeably.

### 4. SimpleBoard  
**Location:** `src/main/java/com/comp2042/model/SimpleBoard.java`

**Changes Made:**  
- Implemented new interface methods:  
  - `getCurrentLevel()`  
  - `swapHoldBrick()`  
- Updated `clearRows()` to return **ClearRow** instead of int.  
- Updated `getViewData()` to include ghost piece + hold brick info.  
- Implemented full hold mechanic with `hasHeldThisTurn` flag.  
- Reset hold flag when generating new bricks.  
- Added complete JavaDoc.

**Reasoning:**  
Better support for ghost pieces, scoring, and animation.  
Returning ClearRow object promotes clean separation between board, UI, and scoring logic.


### 5. Score  
**Location:** `src/main/java/com/comp2042/model/Score.java`

**Changes Made:**  
- Refactored to use **Strategy Pattern** via `ScoringStrategy`.  
- Added strategy field and setter method.  
- Implemented combo tracking:  
  - `incrementCombo()`  
  - `resetCombo()`  
  - `getComboCount()`  
- Added combo multiplier and bonus calculation methods.  
- Delegated scoring logic to strategy implementations.  
- Added complete JavaDoc.

**Reasoning:**  
Scoring is now flexible and extendable.  
Combo system increases gameplay depth and engagement.


## Brick Package

### 6. Brick  
**Location:** `src/main/java/com/comp2042/logic/bricks/Brick.java`

**Changes Made:**  
- Improved JavaDoc explaining interface purpose.  
- Updated `getShapeMatrix()` documentation.

**Reasoning:**  
Better clarity for developers implementing new brick types.


### 7. Brick Implementations  
**Location:** `src/main/java/com/comp2042/logic/bricks/`   
Classes include: `IBrick`, `JBrick`, `LBrick`, `OBrick`, `SBrick`, `TBrick`, `ZBrick`.

**Changes Made:**  
- Updated imports from `com.comp2042.MatrixOperations` → `com.comp2042.util.MatrixOperations`.  
- Added JavaDoc for constructors & key methods.

**Reasoning:**  
Required after package reorganization.  
Documentation ensures consistency across all brick classes.

## Utility Package
### 8. MatrixOperations  
**Location:** `src/main/java/com/comp2042/util/MatrixOperations.java`

**Changes Made:**  
- Moved from `com.comp2042` → `com.comp2042.util`.  
- Added private constructor with `UnsupportedOperationException`.  
- Updated `checkRemoving()` to return a **ClearRow** object with cleared row indices.  
- Added full JavaDoc for all static methods.

**Reasoning:**  
Better organizational structure.  
Utility class enforcement prevents misuse.  
Returning ClearRow supports animations and scoring.

## Main Classes
### 9. Main  
**Location:** `src/main/java/com/comp2042/Main.java`

**Changes Made:**  
- Updated to load **start menu FXML** instead of starting game immediately.  
- Added error handling for missing FXML.  
- Added full JavaDoc.

**Reasoning:**  
Improves user experience by showing a proper entry point.

### 10. StartMenuController  
**Location:** `src/main/java/com/comp2042/StartMenuController.java`

**Changes Made:**  
- Updated imports to reference the refactored controller package.  
- Added JavaDoc.  
- Ensured proper initialization when launching the game.

**Reasoning:**  
Aligns with new package structure and improves maintainability.

## Audio Package
### 11. SoundManager  
**Location:** `src/main/java/com/comp2042/audio/SoundManager.java`

**Changes Made:**  
- Added comprehensive JavaDoc for all audio methods.  
- Documented mute logic, volume handling, and background music looping.

**Reasoning:**  
Improves clarity for developers extending or maintaining audio features.

## Unexpected Problems 
**1. Package Reorganization Issues**
- Moving classes to new packages broke multiple import statements across 20+ files. Had to systematically update all references.

**2. instanceof Removal Complexity**
- Removing instanceof checks required adding new methods to the Board interface, which initially caused compilation errors until all implementations were updated.

**3. Maven Dependency Management**
- Ensuring all test dependencies (JUnit 5) were properly configured in pom.xml for the new test structure.

## Maintenance & Refactoring Documentation
All changes are version-controlled under the branch: maintenance/refactor-1
## Version Control & Branching 
I used Git and GitHub with a maintenance branch: 
- Main branch : `main`
- Maintenance branch : `maintenance-refactor-1`

Key maintenance commits include:
- `refactor: improve BrickRotator safety and extract Renderer to improve GUI structure`
- `maintenance: document MatrixOperations and clarify bounds checking logic`
- `test: add unit tests for MatrixOperations (copy, row-clearing, deep-copy)`

This shows a clear history of purposeful maintenance and testing work rather
than random unstructured edits.

## Refactor 1 - BrickRotator Safety Improvements 
Problem: `BrickRotator.java` previously lacked null-checks and proper bounds validation.
Incorrect inputs caused the rotation logic to break silently.

Solution :
- Added safe guards and fail-fast handling
- Normalized rotation indices
- Improved clarity of rotation operations

## Refactor 2 - Renderer Extraction (GUI Maintainability)
Problem: GuiController was overloaded with responsibilities such as handling input, game logic, drawing the game board and updating brick matrices.This violates the Single Responsible Principle (SRP) and made the class difficult to maintain.

Solution:
I extracted all rendering/drawing logic into a new class: `Renderer.java`
What Renderer now handles :
- Drawing the game grid
- Drawing the falling brick
- Updating the colours of the cells
This reduces duplicated drawing code and increases maintainability. This refactor is visible in the commit : "refactor : improve BrickRotator Safety and extract Renderer to improve GUI structure"

## Refactor 3 - MatrixOperations readability & documentation 
- Added class-level Javadoc explaining that `MatrixOperations` is a utility for:
  - collision detection (`intersect`),
  - merging bricks into the board (`merge`),
  - clearing full rows and calculating score bonus (`checkRemoving`),
  - deep-copying matrices (`copy`, `deepCopyList`).
- Simplified `checkOutOfBound` into clear vertical + horizontal checks instead of a compact boolean expression.
- Added comments to the main methods to make them easier to understand for future maintainers.

## Refactor 4 - SimpleBoard understanding 
`SimpleBoard` is the core game model. I improved its documentation by:
- Adding a class-level comment describing its role:
  - stores the game matrix,
  - tracks the current brick and offset,
  - interacts with `BrickGenerator`, `BrickRotator`, and `Score`,
  - exposes state to the GUI.
- Adding short Javadoc to methods like `newGame()` and `getScore()`.

This makes the central game logic much easier to read and reason about.

## Refactor 5 - Brick System Documentation (`BrickGenerator`, `RandomBrickGenerator`:

The brick system (how bricks are generated and structured) lacked documentation. This made it difficult to understand how the game selects and rotates pieces.
- Added class-level Javadoc explaining the responsibilities of each class.
- Documented how brick rotation states work and how they are stored.
- Added comments to fields to clarify their purpose (e.g., rotationIndex, rotations).
- Added defensive checks (e.g., ensuring rotation lists are not empty).
- Improved maintainability without changing any gameplay behaviour.
This improves understanding of core architecture, safer brick construction and better readability for future maintainers.

## Refactor 6 - RandomBrickGenerator Documentation & Safety Improvements
Problem:
`RandomBrickGenerator` originally had no documentation and minimal clarity around how bricks were queued and selected. It also lacked defensive checks.

- Added full-class level Javadoc describing :
  - How bricks are randomised
  - Queue behaviour (nextBricks)
  - Difference between current and next brick
 - Documented each field with inline comments
 - Added a defensive check inside `getNextBrickc()` to ensure queue never empties
 - Reformatted constructor logic for clarity

## Refactor 7 - Package Reorganization
The project was restructured from one large, flat package into 7 well-organized packages, improving clarity, maintainability, and scalability.
**New Package Structure**
com.comp2042
 ├── controller/        → Game coordination (`GameController`)
 ├── model/             → Core data models (`Board`, `SimpleBoard`, `Score`, `ViewData`)
 ├── ui/                → UI components (`Renderer`, `GameOverPanel`, `NotificationPanel`)
 ├── util/              → Utility helpers (`MatrixOperations`, `HighScoreManager`)
 ├── events/            → Event system (`EventType`, `EventSource`, `MoveEvent`…)
 ├── audio/             → Sound management (`SoundManager`)
 └── logic/bricks/      → Brick types and brick generation system
**Benefits :**
- Better separation of concerns
- Easier navigation
- Cleaner imports and dependencies
- Increased maintainability
## Refactor 8 - Encapsulation & Best Practices 
Several classes were improved to enforce better OOP principles.
**Utility Class Protection**
Utility classes now include private constructors:
private MatrixOperations() {
    throw new UnsupportedOperationException("Utility class cannot be instantiated");
}
- Prevents accidental instantiation and enforces static-only usage.

**Defensive Copying**
Classes such as `ViewData`, `ClearRow`, and `NextShapeInfo` now return copies of internal arrays instead of exposing internal state.

- Prevents external mutation
- Guarantees data integrity

## Refactor 9 - Extracted AnimationManager
A dedicated `AnimationManager` class was created to handle line-clear animations.
Previously, `GuiController` handled:
- Input
- Rendering
- Animations
This violated Single Responsibility Principle.
**Impact**
  - `GuiController` is now smaller and more maintainable.
  - Animations are isolated, easier to test, and easier to extend.

## Refactor 10 - Implemented Strategy Pattern for Scoring 
The scoring system now supports multiple scoring algorithms through the Strategy Pattern.

**New Classes**
- `ScoringStrategy` (interface)
- `StandardScoringStrategy` - default scoring (50*lines^2)
- `AggressiveScoringStrategy` -  advanced scoring (100 * lines^2)
**Updates**
- `Score` now uses a pluggable scoring strategy
- `ScoringStrategyTest` added (11 tests)
**Benefits**
- Flexible scoring behavior
- Easy to add new scoring systems
- Cleaner, more testable design

## Testing Documentation
I added unit tests using JUnit 5 (`org.junit.jupiter`):

## BrickRotatorTest  
**File:** `src/test/java/com/comp2042/BrickRotatorTest.java` 
Covers:
- Rotation behaviour  
  - Confirms bricks rotate forward and backward correctly.  
  - Ensures rotation wraps properly from last state → first, and vice-versa.  

- State validation 
  - Throws an exception if rotation is attempted before state data is set.  
  - Handles null bricks gracefully and rejects invalid operations.  

- Index normalisation
  - Ensures rotation indexes remain within valid bounds.  
  - Prevents negative or out-of-range rotation indexes.  

These tests validate the brick rotation system and protect against illegal state transitions or invalid operations.

## MatrixOperationsTest
File : `src/test/java/com/comp2042/MatrixOperationsTest.java`

Covers: 
- `copy(int[][])`  
  - Verifies that a deep copy of a 2D matrix is created.
  - Checks that the returned matrix has separate row objects but the same values.
  - Confirms that modifying the original matrix does not affect the copy.

- `checkRemoving(int[][])`  
  - Tests the behaviour when one full row is present:
    - `linesRemoved` = 1  
    - `scoreBonus` = 50  
  - Tests the behaviour when two full rows are present:
    - `linesRemoved` = 2  
    - `scoreBonus` = 200 (because the game uses 50 * rows²).

- `deepCopyList(List<int[][]>)`
  - Verifies that each matrix in the input list is copied deeply.
  - Confirms that changing the original matrices does not change the copies.

These tests demonstrate that I understand how row clearing and score bonus work
and provide automated regression checks for future changes.

## ScoreTest
File : `src/test/java/com/comp2042/ScoreTest.java`

Covers :
- Confirms that a new `Score` object starts at 0
- Ensures that calling `add(int)` correctly increments the score
- Ensures that calling `reset()` returns the score to 0
- Uses JavaFX `IntegerProperty`, so tests access values using `.get()`
These tests ensure the scoring system behaves correctly and help prevent regressions in score-related logic.

## ClearRowTest
File : `src/test/java/com/comp2042/ClearRowTest.java`

Covers:
-   `linesRemoved` is stored correctly
-   `scoreBonus` reflects the unexpected bonus value
-   `newMatrix` returns the updated board matrix after row clearing
This verifies that ClearRow behaves as expected, supporting both scoring and board-updating features of the game.

## ViewDataTest  
**File:** `src/test/java/com/comp2042/ViewDataTest.java`

Covers : 
- Defensive copying  
  - Ensures internal board arrays & ghost-piece arrays are not exposed directly.  
  - Confirms that modifications to returned arrays do *not* affect internal state.  

- Ghost piece logic : 
  - Verifies that ghost piece coordinates are calculated and stored correctly.  

- Hold brick handling 
  - Confirms correct behaviour when the hold brick is null.  
  - Ensures hold brick data is safely copied, not leaked.  

These tests ensure the ViewData class maintains encapsulation and safely exposes read-only game state to the UI.

## HighScoreManagerTest  
**File:** `src/test/java/com/comp2042/HighScoreManagerTest.java`
Covers: 
- File persistence 
  - Saves a score to disk and loads it again reliably.  
  - Confirms that an empty or missing score file returns a score of 0.  

- Load/save cycle 
  - Ensures that saved values persist consistently between sessions.  

These tests verify the reliability of the high-score system and ensure it remains stable across file operations.

## SimpleBoardTest  
**File:** `src/test/java/com/comp2042/SimpleBoardTest.java`
Covers: 
- Brick movement & collision
  - Validates correct movement left, right, and downward.  
  - Ensures collision rules behave correctly in all scenarios.  

- Rotation rules
  - Ensures bricks rotate correctly when space allows.  
  - Confirms rotation is blocked when walls or other bricks interfere.  

- Level progression 
  - Tests scoring thresholds and confirms levels increase appropriately.  

- Hold brick handling 
  - Verifies the “one swap per brick” rule.  
  - Ensures hold behaviour resets correctly after a brick locks.  

These tests cover the central gameplay logic and help guarantee consistent Tetris behaviour across future updates.

## Additional Test Suites  
### **MatrixOperationsTest, ClearRowTest, ScoringStrategyTest**
These test suites complement the others by covering:
- Matrix manipulation and defensive copying  
- Line-clearing behaviour and score calculation  
- Strategy-based scoring (Standard vs Aggressive scoring)  
  
## Fixes
## UI Layout Fixes
*Files :* `gameLayout.fxml`, `GuiController.java`
**Update :**
- Corrected misalignment between the main game board and sidebar panels.
- Ensured the grid is properly centered within the window, fixing offset issues where bricks appeared misaligned vertically/horizontally.
- Adjusted sidebar positioning to maintain consistent spacing for Score, Level, Next Brick, and Hold panels.
- Improves overall readability and visual balance of the UI.
- 
## Menu & Pause System Fixes  
*Files* `GuiController.java`, `MainMenu.fxml`, `PauseMenu.fxml`

**Updates:**  
- Fixed pause menu logic so that pausing/unpausing no longer desynchronizes the game state.  
- Updated main menu UI for better consistency and cleaner navigation flow.  
- Ensured menu transitions behave correctly after game over and while pausing.
- 
## Gameplay Logic Fixes  
*Files:* `GuiController.java`, `ClearRow.java`, `GameController.java`
**Updates:**  
- **Restart Timeline Fix:**  
  - Resolved an issue where calling `newGame()` stopped the timeline but did **not** restart it.  
  - This fix restores automatic brick dropping after pressing **N** to start a new game.  
- **ClearRow Improvements:**  
  - Applied minor logic fixes and improved internal behaviour in row-clearing calculations.  
  - Ensured consistency in how score bonus and removed lines are handled.  

## Game Over Screen & User Feedback Fixes  
*Files:* `GuiController.java`, `GameOverPanel.java`, `gameLayout.fxml`
**Updates:**  
- Updated the “Game Over” text to be more visible and readable.  
- Added an on-screen instruction prompting players to press **N** to start a new game.  
- Implemented a fade-in effect for the Game Over screen to improve presentation.  
- Updated main menu background visuals for better clarity and contrast.  

## General Javadoc & Documentation Fixes  
*Files:* Multiple (`ClearRow.java`, `GameController.java`, `ViewData.java`, etc.)
**Updates:**  
- Cleaned up outdated or unclear Javadoc comments.  
- Improved formatting and added missing descriptions for parameters and return types.  
- Ensured consistent documentation style across the project.





