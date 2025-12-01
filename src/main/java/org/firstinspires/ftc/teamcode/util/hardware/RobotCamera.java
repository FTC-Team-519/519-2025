package org.firstinspires.ftc.teamcode.util.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.*;
import org.firstinspires.ftc.teamcode.util.hardware.IntakeColorSensor.pieceType;

import java.util.*;

public class RobotCamera {
    private final AprilTagProcessor camera;
    private final VisionPortal visionPortal;

    public RobotCamera(HardwareMap hardwareMap) {
        camera = AprilTagProcessor.easyCreateWithDefaults(); // create our processor
        visionPortal = VisionPortal.easyCreateWithDefaults( // create our "camera" with a webcam and processor
                hardwareMap.get(WebcamName.class,"camera"),
                camera
        );
        visionPortal.resumeStreaming(); // start streaming because it may have been stopped logically
    }

    public double[] distances() {
        double[] ans = new double[4];
        int index = -1;
        int i = 0;
        /* NOTE:
          This code does, by definition, favor whichever detection is first in the array given
          to us, but it should only scan team-based apriltags, and it shouldn't be possible for
          us to have multiple of the team apriltags within sight at once.
        */
        for(AprilTagDetection detection:camera.getDetections()) {
            if(detection.id==20 || detection.id==24) {
                index = i;
                break;
            }
            i++;
        }
        if(camera.getDetections().isEmpty() || index==-1) {return new double[4];} // catch our nondetections and wrong ones
        AprilTagDetection detection = camera.getDetections().get(index);
        AprilTagPoseFtc pose = detection.ftcPose;
        ans[0] = pose.x;
        ans[1] = pose.y;
        ans[2] = pose.z;
        ans[3] = pose.yaw;
        return ans;
    }

    public int[] getIDs() {
        ArrayList<AprilTagDetection> detections = camera.getDetections();
        int[] Ids = new int[detections.size()];
        int i = 0;
        for(AprilTagDetection detection: detections) {
            Ids[i] = detection.id;
            i++;
        }
        return Ids;
    }

    /*
        May return null, if no motif apriltag is found
        Otherwise returns the respective motif in pieceType array format
     */
    public pieceType[] getMotif() {
        ArrayList<AprilTagDetection> detections = camera.getDetections();
        /* NOTE:
            Similarly to the distances code, this also favors whichever of the detections comes first
            in the array, however this one should be physically impossible to run into, due to a lack
            of conflicting motif apriltags.
         */
        for(AprilTagDetection detection: detections) {
            if(detection.id==21) {
                pieceType[] ans = new pieceType[3];
                ans[0] = pieceType.GREEN;
                ans[1] = pieceType.PURPLE;
                ans[2] = pieceType.PURPLE;
                return ans;
            }
            if(detection.id==22) {
                pieceType[] ans = new pieceType[3];
                ans[0] = pieceType.PURPLE;
                ans[1] = pieceType.GREEN;
                ans[2] = pieceType.PURPLE;
                return ans;
            }
            if(detection.id==23) {
                pieceType[] ans = new pieceType[3];
                ans[0] = pieceType.PURPLE;
                ans[1] = pieceType.PURPLE;
                ans[2] = pieceType.GREEN;
                return ans;
            }
        }
        return null;
    }

    public void stopStreaming() {
        visionPortal.stopStreaming();
    }

    public void resumeStreaming() {
        visionPortal.resumeStreaming();
    }


}
