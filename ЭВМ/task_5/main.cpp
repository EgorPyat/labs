#include <stdio.h>
#include <opencv2/opencv.hpp>
#include <iostream>
#include <cmath>

using namespace cv;
using namespace std;

int main(int argc, char** argv )
{
  CvCapture *capture = cvCreateCameraCapture(0);

  if(!capture) return 0;

  while(1){
    IplImage * image = cvQueryFrame(capture);
    IplImage * frame = cvCloneImage(image);

    if(!image) break;

    for (int y=0; y<image->height / 2; y++) {
      uchar *ptr = (uchar*)(image->imageData + y*image->widthStep);
      for(int x=0; x<image->width / 2;x++){
        ptr[3*x] = 64;
        ptr[3*x+2] = 64;
      }
    }

    for (int y = image->height/2; y<image->height; y++) {
      uchar *ptr = (uchar*)(image->imageData + y*image->widthStep);
      for(int x = image->width/2; x<image->width;x++){
        ptr[3*x] = 192;
        ptr[3*x+2] = 0;
      }
    }

    for (int y = image->height/2; y<image->height; y++) {
      uchar *ptr = (uchar*)(image->imageData + y*image->widthStep);
      for(int x = 0; x<image->width /2;x++){
        ptr[3*x] = 192;
        ptr[3*x+2] = 192;
      }
    }
    for (int y = 0; y<image->height / 2; y++) {
      uchar *ptr = (uchar*)(image->imageData + y*image->widthStep);
      for(int x = image->width/2; x<image->width;x++){
        ptr[3*x] = 0;
        ptr[3*x+2] = 224;
      }
    }
    for (int y = image->height / 2; y < (image->height / 2) + 128; y++) {
      uchar *ptr = (uchar*)(image->imageData + y*image->widthStep);
      for(int x = (image->width / 2) - sqrt(16384 - ((y - image->height / 2))*(y - (image->height / 2))); x < image->width / 2 + sqrt(16384 - ((y - image->height / 2))*(y - (image->height / 2))); x++){
        ptr[3*x] = 12;
        ptr[3*x+2] = 122;
      }
    }

    for (int y = image->height / 2; y > (image->height / 2) - 128; y--) {
      uchar *ptr = (uchar*)(image->imageData + y*image->widthStep);
      for(int x = (image->width / 2) - sqrt(16384 - ((y - image->height / 2))*(y - (image->height / 2))); x < image->width / 2 + sqrt(16384 - ((y - image->height / 2))*(y - (image->height / 2))); x++){
        ptr[3*x] = 128;
        ptr[3*x+2] = 128;
      }
    }

    cvFlip(image, image, 1);

    cvShowImage("test1", image);
    cvShowImage("test2",frame);
    char c = cvWaitKey(33);
    if(c == 27) break;
  }

  cvReleaseCapture(&capture);
  cvDestroyWindow("test");

  return 0;
}
