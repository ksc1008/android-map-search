package ksc.campus.tech.kakao.map.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapReadyCallback
import com.kakao.vectormap.MapView
import ksc.campus.tech.kakao.map.R
import java.lang.Exception

class KakaoMapFragment : Fragment() {
    lateinit private var kakaoMap: MapView

    private fun initiateKakaoMap(view:View){
        kakaoMap = view.findViewById(R.id.kakao_map_view)
        kakaoMap.start(object: MapLifeCycleCallback() {
            override fun onMapDestroy() {
            }

            override fun onMapError(p0: Exception?) {
            }

        },
            object: KakaoMapReadyCallback(){
                override fun onMapReady(km: KakaoMap) {
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