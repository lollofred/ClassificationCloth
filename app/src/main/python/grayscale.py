import cv2

def main(img):
    img = cv2.imread(img,0)
    image_bytes = cv2.imencode('.jpg', img)[1].tobytes()
    return image_bytes

