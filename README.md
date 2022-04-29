This digital image processing library is for my cs102 project. This library will preproccess the image before using neural network to read the text.<br />
This library is completed.<br />
<br />
This library can binarize an image and then parse lines and characters of texts.<br />
It uses hard coded algorithms for this purpose. For binarizing the image I used Sauvola binarization algorithm.<br />
Then I am applying Gaussian Smoothing.<br />
After smoothing I am applyng some line detection kernels with different sizes which have form:<br />
 1  1  1  1  1  1  1  1<br />
-1 -1 -1 -1 -1 -1 -1 -1<br />
-1 -1 -1 -1 -1 -1 -1 -1<br />
 1  1  1  1  1  1  1  1<br />
For parsing characters I basically rotated the lines and apply smaller versions of these filters which means these filters has the form:<br />
1 -1 -1 1<br />
1 -1 -1 1<br />
1 -1 -1 1<br />
1 -1 -1 1<br />
1 -1 -1 1<br />
1 -1 -1 1<br />
1 -1 -1 1<br />
1 -1 -1 1<br />
