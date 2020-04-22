package brainfuckide.util;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 *
 * @author Nelson Earle (nwewnh)
 */
public class PulseAnimation extends Transition {

    public interface Target {
        Region get();
    }

    private final Target target;

    public PulseAnimation(Target target, Duration duration) {
        super();

        this.target = target;

        this.setCycleDuration(duration.divide(2));
        this.setAutoReverse(true);
        this.setCycleCount(2);
        this.setInterpolator(Interpolator.LINEAR);
    }

    @Override
    protected void interpolate(double frac) {
        Region region = this.target.get();
        double radius = Math.min(region.getWidth(), region.getHeight()) * .85;
        Color shadowColor = new Color(1, 1, 1, .6 * frac);
        region.setEffect(new InnerShadow(
            BlurType.THREE_PASS_BOX, shadowColor, radius, 0, 0, 0
        ));
    }

}
