# SolarProgressView
类似于太阳花形状的自定义进度条


    <com.mqt.solarprogressview.SolarView
        android:id="@+id/sv_light"
        android:layout_width="120dp"
        android:layout_height="120dp"
        app:color="@color/colorAccent"
        app:lightRadius="6dp"
        app:max="8"
        app:pregress="0"
        app:solarScale="0.26" />
    lightRadius：四周小圆的半径
    max：进度最大值
    pregress：当前进度值
    solarScale：中间“太阳”占整个控件的百分比0-1
