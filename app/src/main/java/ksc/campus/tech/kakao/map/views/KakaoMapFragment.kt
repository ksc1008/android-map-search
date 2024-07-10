package ksc.campus.tech.kakao.map.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapReadyCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraPosition
import com.kakao.vectormap.camera.CameraUpdate
import com.kakao.vectormap.camera.CameraUpdateFactory
import ksc.campus.tech.kakao.map.R
import java.lang.Exception

class KakaoMapFragment : Fragment() {
    lateinit private var kakaoMap: MapView

    private fun initiateKakaoMap(view: View) {
        kakaoMap = view.findViewById(R.id.kakao_map_view)
        kakaoMap.start(object: MapLifeCycleCallback() {
            override fun onMapDestroy() {
            }

            override fun onMapError(e: Exception?) {
                Log.e("KSC", e?.message?:"")
            }

        },
            object: KakaoMapReadyCallback(){
                override fun onMapReady(km: KakaoMap) {
                    val builder = CameraPosition.Builder()
                    builder.position = LatLng.from(35.8905341232321, 128.61213266480294)
                    builder.tiltAngle = 0.0
                    builder.zoomLevel = 15
                    val camUpdate = CameraUpdateFactory.newCameraPosition(CameraPosition.from(builder))
                    km.moveCamera(camUpdate)
                }
            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kakao_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initiateKakaoMap(view)
        super.onViewCreated(view, savedInstanceState)
    }
}