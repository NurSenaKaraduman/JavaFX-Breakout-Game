import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Oyundaki her bir tuğlayı temsil eden class.
 * Her tuğla kendi konumunu, boyutunu ve durumunu bilir.
 */
public class Brick {

    // ── Konum ve boyut ──────────────────────────────────────
    private double x;       // Tuğlanın sol kenarının X koordinatı
    private double y;       // Tuğlanın üst kenarının Y koordinatı
    private double width;   // Tuğla genişliği
    private double height;  // Tuğla yüksekliği

    // ── Durum ───────────────────────────────────────────────
    private boolean alive;  // true → ekranda görünür, false → kırıldı

    // ── Görünüm ─────────────────────────────────────────────
    private Color color;    // Tuğlanın rengi

    // ────────────────────────────────────────────────────────
    // CONSTRUCTOR — Tuğla ilk oluşturulduğunda ne bilmeli?
    // ────────────────────────────────────────────────────────
    public Brick(double x, double y, double width, double height, Color color) {
        this.x      = x;
        this.y      = y;
        this.width  = width;
        this.height = height;
        this.color  = color;
        this.alive  = true; // Tuğla doğduğunda hep sağlam başlar
    }

    // ────────────────────────────────────────────────────────
    // DRAW — Bu tuğlayı ekrana çiz
    // ────────────────────────────────────────────────────────
    public void draw(GraphicsContext gc) {

        // Kırılmış tuğlaları çizmiyoruz, direkt çık
        if (!alive) return;

        // Tuğlanın dolgu rengi
        gc.setFill(color);
        gc.fillRoundRect(x, y, width, height, 6, 6); // 6,6 → hafif yuvarlak köşe

        // Tuğlanın kenar çizgisi (daha güzel görünsün diye)
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1.5);
        gc.strokeRoundRect(x, y, width, height, 6, 6);
    }

    // ────────────────────────────────────────────────────────
    // INTERSECTS — Top bu tuğlaya çarptı mı?
    // ballX, ballY → topun merkez koordinatları
    // ballR        → topun yarıçapı
    // ────────────────────────────────────────────────────────
    public boolean intersects(double ballX, double ballY, double ballR) {

        // Kırık tuğlayla çarpışma kontrolü yapma
        if (!alive) return false;

        // Topun merkezine en yakın tuğla noktasını bul
        // (Bu "AABB - Circle" çarpışma algoritmasıdır)
        double nearestX = clamp(ballX, x, x + width);
        double nearestY = clamp(ballY, y, y + height);

        // Top merkezi ile o en yakın noktanın arası kaç piksel?
        double distX = ballX - nearestX;
        double distY = ballY - nearestY;

        // Pisagor teoremi: mesafe² = x² + y²
        // Eğer mesafe < yarıçap ise top tuğlaya girmiş.
        double distSquared = (distX * distX) + (distY * distY);  
        return distSquared <= (ballR * ballR);
    }

    // ────────────────────────────────────────────────────────
    // CLAMP — Yardımcı metod
    // Bir sayıyı [min, max] aralığına sıkıştırır
    // Örnek: clamp(150, 0, 100) → 100 döner
    // ────────────────────────────────────────────────────────
    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    // ────────────────────────────────────────────────────────
    // DESTROY — Tuğlayı kır
    // ────────────────────────────────────────────────────────
    public void destroy() {
        this.alive = false; // Artık çizilmeyecek ve çarpışma vermeyecek
    }

    // ────────────────────────────────────────────────────────
    // GETTER — Dışarıdan alive durumu okunabilsin
    // ────────────────────────────────────────────────────────
    public boolean isAlive() {
        return alive;
    }
}