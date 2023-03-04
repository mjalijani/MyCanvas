package com.example.mycanvas.view

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.mycanvas.R
import com.example.mycanvas.model.GeoData
import com.google.gson.Gson
import org.osmdroid.config.Configuration.getInstance
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polygon
import org.osmdroid.views.overlay.Polyline
import java.io.IOException
import java.io.InputStream


class MainActivity : AppCompatActivity() {
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1
    private lateinit var map: MapView
    private val polyline = ArrayList<List<Any>>()
    private val polygon = ArrayList<List<Any>>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        //setting this before the layout is inflated is a good idea
        //it 'should' ensure that the map has a writable location for the map cache, even without permissions
        //if no tiles are displayed, you can try overriding the cache path using Configuration.getInstance().setCachePath
        //see also StorageUtils
        //note, the load method also sets the HTTP User Agent to your application's package name, if you abuse osm's
        //tile servers will get you banned based on this string.

        //inflate and create the map
        setContentView(R.layout.activity_main)


        map = findViewById(R.id.map)
        map.setTileSource(TileSourceFactory.MAPNIK)

        val mapController = map.controller
        mapController.setZoom(15.5)
        val startPoint = GeoPoint(35.7844311386761, 51.360950675604784)
        mapController.setCenter(startPoint)
        val geoData = loadJSONFile()

        // fill the polygon & polyline
        geoData?.let {
            polygon.addAll(geoData.features[8].geometry.coordinates)
            for (i in 0..7) {
                polyline.add(geoData.features[i].geometry.coordinates)
            }
        }


//add your points here
        val osmPolygon = Polygon();    //see note below
        val geoPoints = ArrayList<GeoPoint>()
        for (poly in polygon[0]) {
            val geos: List<Double> = poly as List<Double>
            geoPoints.add(GeoPoint(geos[1], geos[0]))
        }
        geoPoints.addAll(osmPolygon.actualPoints)
        geoPoints.add(geoPoints[0]);    //forces the loop to close(connect last point to first point)
        osmPolygon.fillPaint.color = getColor(R.color.black_transparent) //set fill color
        osmPolygon.points = geoPoints
        map.overlays.add(osmPolygon)


        val osmPolygon1 = Polygon();    //see note below
        val geoPoints1 = ArrayList<GeoPoint>()
        for (poly in polygon[1]) {
            val geos: List<Double> = poly as List<Double>
            geoPoints1.add(GeoPoint(geos[1], geos[0]))
        }
        geoPoints1.addAll(osmPolygon1.actualPoints)
        geoPoints1.add(geoPoints1[0]);    //forces the loop to close(connect last point to first point)
        osmPolygon1.fillPaint.color = getColor(R.color.white_transparent) //set fill color
        osmPolygon1.points = geoPoints1
        map.overlays.add(osmPolygon1)


        val line = ArrayList<Polyline>()  //see note below

        val lineGeo = ArrayList<GeoPoint>()

        for (i in polyline.indices) {
            line.add(Polyline())
            lineGeo.clear()

            for (polyLines in polyline[i]) {
                val geos: List<Double> = polyLines as List<Double>
                lineGeo.add(GeoPoint(geos[1], geos[0]))
                line[i].setPoints(lineGeo)
            }
            map.overlays.add(line[i])
        }
    }

    private fun loadJSONFile(): GeoData? {
        val json: String? = try {
            val `is`: InputStream = assets.open("Network.geojson")
            val size: Int = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return Gson().fromJson(json, GeoData::class.java)
    }

    override fun onResume() {
        super.onResume()
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume() //needed for compass, my location overlays, v6.0.0 and up
    }

    override fun onPause() {
        super.onPause()
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause()  //needed for compass, my location overlays, v6.0.0 and up
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val permissionsToRequest = ArrayList<String>()
        var i = 0
        while (i < grantResults.size) {
            permissionsToRequest.add(permissions[i])
            i++
        }
        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }
}
