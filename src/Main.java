import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;


public class Main extends Application {

// Sahne boyutu
    static final int W = 480, H = 640;

// Top
    double ballX = W / 2.0, ballY = H / 2.0;
    double ballDX = 3, ballDY = -3;
    final double BALL_R = 10;

// Panel
    double paddleX = W / 2.0 - 40;
    final double PADDLE_Y = H - 40;
    final double PADDLE_W = 80, PADDLE_H = 12;

// Mouse takibi
    double mouseX = W / 2.0;
//----------------------------------------------------
    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(W, H);
        GraphicsContext gc = canvas.getGraphicsContext2D();

// Mouse hareketi
        canvas.setOnMouseMoved(e -> mouseX = e.getX());

// Game loop
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                draw(gc);
            }
        };
        timer.start();

        StackPane root = new StackPane(canvas);
        stage.setScene(new Scene(root, W, H));
        stage.setTitle("Breakout");
        stage.show();
    }

    void update() {
// Panel mouse'u takip etsin
        paddleX = mouseX - PADDLE_W / 2;

// Top hareketi
        ballX += ballDX;
        ballY += ballDY;

 // Sol/sağ duvar
        if (ballX - BALL_R < 0 || ballX + BALL_R > W) ballDX *= -1;

// Üst duvar
        if (ballY - BALL_R < 0) ballDY *= -1;

// Panel çarpışması
        if (ballY + BALL_R >= PADDLE_Y
                && ballX >= paddleX
                && ballX <= paddleX + PADDLE_W) {
            ballDY *= -1;
            ballY = PADDLE_Y - BALL_R;
        }

// Top düştü mü
        if (ballY > H + 20) {
            ballX = W / 2.0;
            ballY = H / 2.0;
            ballDY = -3;
        }
    }
//-----------------------------------------------------------
    void draw(GraphicsContext gc) {
// Arkaplan
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, W, H);

// Top
        gc.setFill(Color.WHITE);
        gc.fillOval(ballX - BALL_R, ballY - BALL_R, BALL_R * 2, BALL_R * 2);

// Panel
        gc.setFill(Color.CORNFLOWERBLUE);
        gc.fillRoundRect(paddleX, PADDLE_Y, PADDLE_W, PADDLE_H, 8, 8);
    }

    public static void main(String[] args) {
        launch();
    }
}