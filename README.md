

# Paradox Cat  Test Challenge 


## **25<sup>th</sup> May 2019**


# GETTING STARTED	

These instructions will get you a copy of the project up and running on your local machine:


1. Clone the [project repository](https://github.com/mfathy/wav-decoder) from Github.

    ```
    git clone https://github.com/mfathy/wav-decoder.git
    ```

2. Open **IntelliJ IDEA **or any IDE for Java, Select File | Open... and point to the the project, wait until the project syncs and builds successfully.
3. Run the project using IntelliJ.


# DISCUSSION 


## Overview

The project is a simple java console application for decoding wav files into binary data using AFSK.


## Decoding

Decoding is done by:



*   Reading sample data from the wav file at once.  
*   Calculating waveform pulse widths, calculation is done using a brute force algorithm by looping through all the sample data. This algorithm time complexity is **O(N)**, where **N** is the number of samples. 
*   Classifying those pulses based on the formula provided with task to detect which one is a One signal or Zero signal:
    *   A one signal is a rectangle signal of t = 320 microseconds.
    *   A zero signal is a rectangle signal of t = 640 microseconds.
    *   The algorithm used to do the classification is a brute force algorithm search and classify one signal pulses and zero signal pulses. This algorithm time complexity is **O(N<sup>2</sup>)**, where **N **is the number of unique pulse widths. 
*   Building the bit stream to have a binary representation.
*   Building the byte stream.
*   Finding the start bytes.
*   Constructing 64 byte messages to be decoded.


## 


## Testing

I have included the required Unit tests with the project.


### **Libraries**



*   [Audio-analysis](https://code.google.com/archive/p/audio-analysis/): An audio library features simple Wave file reading, mono output to the audio device and a couple of audio analysis classes that facilitate onset detection (FFT, post processing etc.).


## **Thank you.**
