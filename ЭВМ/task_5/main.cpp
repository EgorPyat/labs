#include <stdio.h>
#include <opencv2/opencv.hpp>
#include <iostream>
using namespace cv;
using namespace std;
int main(int argc, char** argv )
{
  CvCapture *capture = cvCreateCameraCapture(0);

  if(!capture) return 0;

  while(1){
    IplImage * image = cvQueryFrame(capture);
    if(!image) break;

    for (int y=0; y<image->height; y++) {
      uchar *ptr = (uchar*)(image->imageData + y*image->widthStep);
      for(int x=0; x<image->width;x++){
        ptr[3*x] = 128;
        ptr[3*x+2] = 128;
      }

  }
  cvShowImage("test", image);
  char c = cvWaitKey(33);
  if(c == 27) break;
}
  cvReleaseCapture(&capture);
  cvDestroyWindow("test");

  return 0;
}
