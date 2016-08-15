definition(
    name: "Equalize House Temperature",
    namespace: "ytechie",
    author: "Jason Young",
    description: "If the second floor temperature is more than the main floor, run the furnace fan to equalize",
    category: "Family",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("Temperature Sensors") {
        input "mainFloorTempSensor", "capability.temperatureMeasurement"
        input "secondFloorTempSensor", "capability.temperatureMeasurement"
        
        input "furnaceFan", "capability.thermostat"        
	}
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
    subscribe(mainFloorTempSensor, "temperature", tempChange)
    subscribe(secondFloorTempSensor, "temperature", tempChange)
}

def tempChange(evt) {
  log.debug "Main floor temp: " + mainFloorTempSensor.latestValue("temperature")
  log.debug "Second floor temp: " + secondFloorTempSensor.latestValue("temperature")
  
  if(secondFloorTempSensor.latestValue("temperature") + 4 > mainFloorTempSensor.latestValue("temperature")) {
  	furnaceFan.fanOn();
    log.debug "Turning furnace fan on to equalize temperature"
  } else {
  	furnaceFan.fanAuto();
    log.debug "Turning furnace fan back to automatic"
  }
}