package com.example.pedometerappkt.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.pedometerappkt.R
import com.example.pedometerappkt.db.Session
import com.example.pedometerappkt.other.Constants
import com.example.pedometerappkt.other.Constants.ACTION_PAUSE_SERVICE
import com.example.pedometerappkt.other.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.pedometerappkt.other.Constants.ACTION_STOP_SERVICE
import com.example.pedometerappkt.other.Constants.MAP_ZOOM
import com.example.pedometerappkt.other.Constants.POLYLINE_COLOR
import com.example.pedometerappkt.other.Constants.POLYLINE_WIDTH
import com.example.pedometerappkt.other.TrackingUtility
import com.example.pedometerappkt.services.Polyline
import com.example.pedometerappkt.services.TrackingService
import com.example.pedometerappkt.ui.viewmodels.MainViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_tracking.*
import kotlinx.android.synthetic.main.main.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.*
import com.example.pedometerappkt.other.Constants.REQUEST_CODE_LOCATION_PERMISSION
import javax.inject.Inject
import kotlin.math.round
import android.view.animation.DecelerateInterpolator

import android.animation.ObjectAnimator
import androidx.activity.viewModels
import com.example.pedometerappkt.db.Shop
import com.example.pedometerappkt.ui.viewmodels.ShopViewModel
import timber.log.Timber


@AndroidEntryPoint
class TrackingFragment:Fragment(R.layout.main), EasyPermissions.PermissionCallbacks {

    private val viewModel: MainViewModel by viewModels()

    private var isTracking = false
    private var pathPoints = mutableListOf<Polyline>()

    private var map: GoogleMap? = null

    private var currentTimeInMillis = 0L

    private var menu: Menu? = null



   /* @set:Inject
    var weight = 80f
*/
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissions()
        progressBar.max = 100;

        if(!isTracking) {
            sendCommandToService(Constants.ACTION_START_OR_RESUME_SERVICE)
            isTracking = true
        }

        //toggleRun()

        mapView.onCreate(savedInstanceState)
        /*btnToggleRun.setOnClickListener{
            toggleRun()
        }*/

        /*btnFinishRun.setOnClickListener{
            zoomToSeeWholeTrack()
            endSessionAndSaveToDb()
        }
*/

        mapView.getMapAsync{
            map = it
            addAllPolylines()
        }

        subscribeToObservers()
    }
    
    private fun subscribeToObservers(){
        TrackingService.isTracking.observe(viewLifecycleOwner, Observer { 
            updateTracking(it)
        })
        
        TrackingService.pathPoints.observe(viewLifecycleOwner, Observer {
            pathPoints = it
            addLatestPolyline()
            moveCameraToUser()
        })

        TrackingService.timeRunInMillis.observe(viewLifecycleOwner, Observer {
            currentTimeInMillis = it
            val formattedTime = TrackingUtility.getFormattedStopWatchTime(currentTimeInMillis, true)
            //tvTimer.text = formattedTime
        })
    }
    
    private fun toggleRun(){
        if(isTracking){
            //menu?.getItem(0)?.isVisible = true
            sendCommandToService(ACTION_PAUSE_SERVICE)
        }else{
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_tracking_menu, menu)
        this.menu = menu
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        if(currentTimeInMillis > 0L){
            this.menu?.getItem(0)?.isVisible = true
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.miCancelTracking ->{
                showCancelTrackingDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun showCancelTrackingDialog(){
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle("Cancel Tracking?")
            .setMessage("Are you sure to cancel the session and delete all its data?")
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton("Yes"){_, _ ->
                stopSession()
            }
            .setNegativeButton("No"){ dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .create()
        dialog.show()
    }

    private fun stopSession(){
        sendCommandToService(ACTION_STOP_SERVICE)
        findNavController().navigate(R.id.action_trackingFragment_to_sessionFragment)
    }


    private fun updateTracking(isTracking: Boolean){
        this.isTracking = isTracking
        if(!isTracking){
          //  btnToggleRun.text = "Start"
          //  btnFinishRun.visibility = View.VISIBLE
        }else{
           // btnToggleRun.text = "Stop"
            //menu?.getItem(0)?.isVisible = true
           // btnFinishRun.visibility = View.GONE
        }
    }

    private fun moveCameraToUser(){
        if(pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()){
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    MAP_ZOOM
                )
            )
        }
    }

    private fun zoomToSeeWholeTrack(){
        val bounds = LatLngBounds.Builder()
        for(polyline in pathPoints){
            for(pos in polyline){
                bounds.include(pos)
            }
        }

        map?.moveCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds.build(),
                mapView.width,
                mapView.height,
                (mapView.height * 0.05f).toInt()
            )
        )
    }

    private fun endSessionAndSaveToDb(){
        map?.snapshot { bmp ->
            var distanceInMeters = 0
            for(polyline in pathPoints){
                distanceInMeters += TrackingUtility.calculatePolylineLength(polyline).toInt()
            }
            val avgSpeed = round((distanceInMeters / 1000f) / (currentTimeInMillis / 1000 / 60 / 60) * 10) / 10
            val dateTimestamp = Calendar.getInstance().timeInMillis
            //val caloriesBurned = ((distanceInMeters / 1000) * weight).toInt()
            val steps = (distanceInMeters / 0.7).toInt()
            val coins = (steps / 1000).toInt()
            //val session = Session(bmp, dateTimestamp, avgSpeed, steps, distanceInMeters, currentTimeInMillis, caloriesBurned, coins)
            //viewModel.insertSession(session)
            Snackbar.make(
                requireActivity().findViewById(R.id.rootView),
                "Session saved successfully",
                Snackbar.LENGTH_LONG
            ).show()
            stopSession()
        }
    }

    private fun addAllPolylines(){
        for(polyline in pathPoints){
            val polylineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .addAll(polyline)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun addLatestPolyline(){

        if(pathPoints.isNotEmpty() && pathPoints.last().size > 1){
            val preLastLatLng = pathPoints.last()[pathPoints.last().size - 2]
            val lastLatLng = pathPoints.last().last()
            val polylineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .add(preLastLatLng)
                .add(lastLatLng)
            map?.addPolyline(polylineOptions)
            countAndDisplayResults()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun countAndDisplayResults(){
        var distanceInMeters = 0

        Timber.d("TIME $currentTimeInMillis")
        distanceInMeters += TrackingUtility.calculatePolylineLength(pathPoints.last()).toInt()
        val avgSpeed = round((distanceInMeters / 1f) / (currentTimeInMillis / 1000) * 10) / 10
        Timber.d("Dist $distanceInMeters")
        Timber.d("Speed $avgSpeed")
        val dateTimestamp = Calendar.getInstance().timeInMillis
        val caloriesBurned = ((distanceInMeters / 1000) * 80f).toInt()
        val steps = (distanceInMeters / 0.7).toInt()
        val coins = (steps / 1000).toInt()
        //val session = Session(bmp, dateTimestamp, avgSpeed, steps, distanceInMeters, currentTimeInMillis, caloriesBurned, coins)
        //viewModel.insertSession(session)
        tv_distance.text = "Расстояние\n$distanceInMeters";
        tv_speed.text = "Скорость\n$avgSpeed";
        tv_calories.text = "Калории\n$caloriesBurned";
        tv_coins.text = "Монеты\n$coins";
        tv_steps.text = "$steps"
        progressBar.setProgress(steps)
        val session = Session(dateTimestamp, avgSpeed, steps, distanceInMeters, currentTimeInMillis, caloriesBurned, coins)
        viewModel.insertSession(session)
        Timber.d("Val ${viewModel.sessionsSortedByDate.value.toString()}")
    }

    private fun sendCommandToService(action: String) =
        Intent(requireContext(), TrackingService::class.java).also{
            it.action = action
            requireContext().startService(it)
        }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
       mapView?.onSaveInstanceState(outState)
    }

    private fun requestPermissions(){
        if(TrackingUtility.hasLocationPermissions(requireContext())){
            return
        }
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this app",
                Constants.REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
        else{
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this app",
                Constants.REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
            AppSettingsDialog.Builder(this).build().show()
        } else{
            requestPermissions()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

}