package com.yvision.fragment;


import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yvision.MyChangeCameraActivity;
import com.yvision.R;
import com.yvision.utils.ImageUtils;


/**
 * 拍照后图片预览和选择（重拍/确定）
 */
public class EditSavePhotoFragment extends Fragment {

    //    public static final String TAG = EditSavePhotoFragment.class.getSimpleName();
    public static final String TAG = "EditSavePhotoFragment";
    public static final String BITMAP_KEY = "bitmap_byte_array";
    public static final String ROTATION_KEY = "rotation";
    public static final String COVER_HEIGHT_KEY = "cover_height";
    public static final String IMAGE_HEIGHT_KEY = "image_height";

    public static Fragment newInstance(byte[] data, int rotation, int coverHeight, int imageViewHeight) {
        Fragment fragment = new EditSavePhotoFragment();

        Bundle bundle = new Bundle();
        bundle.putByteArray(BITMAP_KEY, data);
        bundle.putInt(ROTATION_KEY, rotation);
        bundle.putInt(COVER_HEIGHT_KEY, coverHeight);
        bundle.putInt(IMAGE_HEIGHT_KEY, imageViewHeight);

        fragment.setArguments(bundle);//onViewCreated中调用
        return fragment;
    }

    public EditSavePhotoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_save_photo, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int rotation = getArguments().getInt(ROTATION_KEY);
        int coverHeight = getArguments().getInt(COVER_HEIGHT_KEY);
        int imageViewHeight = getArguments().getInt(IMAGE_HEIGHT_KEY);
        byte[] data = getArguments().getByteArray(BITMAP_KEY);

        final View topCoverView = view.findViewById(R.id.cover_top_view);
        final View btnCoverView = view.findViewById(R.id.cover_bottom_view);
        final ImageView photoImageView = (ImageView) view.findViewById(R.id.photo);

        photoImageView.getLayoutParams().height = imageViewHeight;
        photoImageView.getLayoutParams().width = imageViewHeight;
        //        topCoverView.getLayoutParams().height = coverHeight;
        //        btnCoverView.getLayoutParams().height = coverHeight;
        Log.d(TAG, "topCoverView = " + coverHeight);
        Log.d(TAG, "photoImageView = " + imageViewHeight);
        Log.d(TAG, "btnCoverView = " + coverHeight);
        //图片旋转处理
        rotatePicture(rotation, data, photoImageView);

        view.findViewById(R.id.save_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePicture();
            }
        });
    }

    private void rotatePicture(int rotation, byte[] data, ImageView photoImageView) {
        Bitmap bitmap = ImageUtils.decodeSampledBitmapFromByte(getActivity(), data);//图片保存成bitmap

        //
        Log.d(TAG, "original bitmap width " + bitmap.getWidth() + " height " + bitmap.getHeight());
        if (rotation != 0) {
            Bitmap oldBitmap = bitmap;

            Matrix matrix = new Matrix();

            //matrix.postRotate(rotation);此处if我自己添加，原因是有一种情况，图片拍完后180读旋转显示，这种情况可以正常用，但是仍无发解决
            if(rotation == 180){
                Log.d(TAG, "rotatePicture: rotation == 180");
                matrix.postRotate(0);
            }else{
                Log.d(TAG, "rotatePicture: rotation != 180");
                matrix.postRotate(rotation);
            }

            bitmap = Bitmap.createBitmap(oldBitmap, 0, 0, oldBitmap.getWidth(), oldBitmap.getHeight(), matrix, false
            );

            oldBitmap.recycle();
        }

        photoImageView.setImageBitmap(bitmap);
    }

    private void savePicture() {
        ImageView photoImageView = (ImageView) getView().findViewById(R.id.photo);

        Bitmap bitmap = ((BitmapDrawable) photoImageView.getDrawable()).getBitmap();
        //保存图片的路径
        Uri photoUri = ImageUtils.savePicture(getActivity(), bitmap);
        ((MyChangeCameraActivity) getActivity()).returnPhotoUri(photoUri);
    }
}
