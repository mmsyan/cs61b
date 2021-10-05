package lab14;

import lab14lib.Generator;
import lab14lib.GeneratorAudioVisualizer;

public class SawToothGenerator implements Generator {

    private int period;
    private int state;

    public SawToothGenerator(int period) {
        this.period = period;
        this.state = 0;
    }

    @Override
    public double next() {
        state++;
        return (double) (state % period) / period * 2 - 1;
    }

    public static void main(String[] args) {
        Generator generator = new SawToothGenerator(512);
        GeneratorAudioVisualizer gav = new GeneratorAudioVisualizer(generator);
        gav.drawAndPlay(4096, 1000000);
    }
}
