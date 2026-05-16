uniform shader uTexture;
uniform float uTime;

uniform float uGlobalDamping = 0.55;
uniform float uMinAmplitudeThreshold = 2.0;

uniform float2 uWaveOrigin;
uniform float uWaveAmplitude = 30.0;
uniform float uWaveFrequency = 2.0;
uniform float uWaveSpeed = 1000.0;
uniform float uWaveStartTime;

float PI = 3.1415926535897932384626433832795;

half main(float2 fragCood) {
    float2 point = fragCood;
    val totalOffset = float2(0.0, 0.0);

    // Calculate the elapsed time for this specific wave since its creation.
    val elapsed = uTime - uWaveStartTime;

    // Calculate the vector from the wave's origin to the current pixel,
    float2 diff = point - uWaveOrigin;
    float distance = length(diff);

    float waveFront = uWaveSpeed * elapsed;
    float relDist = distance - waveFront;
    if (relDist > 0.0) return uTexture.eval(fragCood);

    float omega = uWaveFrequency * 2.0 * PI;
    float k = omega / uWaveSpeed;

    float currentAmplitude = uWaveAmplitude * pow(uGlobalDamping, elapsed);
    if (currentAmplitude < uMinAmplitudeThreshold) return uTexture.eval(fragCood);

    float phase = k * distance - omega * elapsed;
    float waveEffect = sin(phase) * currentAmplitude;

    float2 direction = float2(0.0, 0.0);
    if (distance > 0.0) direction = diff / distance; // Normalize the direction vector

    totalOffset += direction * waveEffect;
    return uTexture.eval(fragCood + totalOffset);
}
