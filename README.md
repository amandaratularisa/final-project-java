# Pixel Heroes - Platformer Game

Game platformer 2D seperti Mario Bros yang dibuat dengan Java dan JavaFX.

## Cerita Game

Anda berperan sebagai pahlawan yang harus menyelamatkan kerajaan dari Shadow Lord yang telah mencuri Crystal of Light. Anda harus melewati 3 level berbahaya untuk mendapatkan kembali kristal tersebut!

## Fitur Game

- **3 Level dengan kesulitan yang meningkat**
- **Sistem storyline** yang menceritakan petualangan Anda
- **Musuh AI** yang berpatroli di area tertentu
- **Sistem koin** untuk dikumpulkan
- **Fisika platformer** yang realistis (gravitasi, lompat, collision)
- **Sistem kesehatan** pemain
- **Menu dan layar kemenangan**

## Kontrol Game

- **ARROW LEFT/RIGHT** - Bergerak kiri/kanan
- **SPACE** - Lompat
- **ENTER** - Mulai game / Lanjut cerita
- **ESC** - Kembali ke menu

## Cara Bermain

1. Hindari atau kalahkan musuh dengan melompat di atas mereka
2. Kumpulkan koin emas untuk poin
3. Capai garis finish hijau di setiap level
4. Jangan jatuh ke jurang!

## Persyaratan

- Java JDK 11 atau lebih baru
- JavaFX SDK 11 atau lebih baru
- Maven (opsional, untuk build otomatis)

## Cara Menjalankan

### Opsi 1: Menggunakan Maven

\\\ash
mvn clean javafx:run
\\\

### Opsi 2: Kompilasi Manual

1. Download JavaFX SDK dari https://openjfx.io/
2. Extract JavaFX SDK ke folder (misalnya C:\javafx-sdk-17)
3. Jalankan build script:

\\\ash
build.bat
\\\

atau kompilasi manual:

\\\ash
javac --module-path "C:\path\to\javafx-sdk\lib" --add-modules javafx.controls,javafx.graphics -d out src\main\java\com\game\mario\*.java

java --module-path "C:\path\to\javafx-sdk\lib" --add-modules javafx.controls,javafx.graphics -cp out com.game.mario.Main
\\\

### Opsi 3: Menggunakan IDE (IntelliJ IDEA / Eclipse)

1. Import project ke IDE
2. Tambahkan JavaFX library ke project
3. Run Main.java

## Struktur Project

\\\
projekbusung/
+-- src/
�   +-- main/
�       +-- java/
�       �   +-- com/
�       �       +-- game/
�       �           +-- mario/
�       �               +-- Main.java           (Entry point)
�       �               +-- MenuScreen.java     (Menu utama)
�       �               +-- GameController.java (Game loop)
�       �               +-- Player.java         (Karakter pemain)
�       �               +-- Enemy.java          (Musuh)
�       �               +-- Platform.java       (Platform & koin)
�       �               +-- GameLevel.java      (Level management)
�       �               +-- StoryManager.java   (Sistem cerita)
�       +-- resources/
+-- pom.xml
+-- build.bat
+-- README.md
\\\

## Tips Bermain

- Level 1: Relatif mudah, gunakan untuk terbiasa dengan kontrol
- Level 2: Platform melayang lebih banyak, timing lompatan penting
- Level 3: Level tersulit dengan platform sempit dan banyak musuh
- Kalahkan musuh dengan melompat di atas mereka
- Kumpulkan semua koin untuk skor maksimal!

## Kredit

Game ini dibuat menggunakan JavaFX sebagai pembelajaran pemrograman game 2D.

Selamat bermain! ??
