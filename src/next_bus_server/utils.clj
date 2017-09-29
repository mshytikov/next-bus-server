(ns next-bus-server.utils)

(defn haversine
  "Returns distance between 2 points in km"
  [{lon1 :lon lat1 :lat} {lon2 :lon lat2 :lat}]
  (let [R 6372.8 ; kilometers
        dlat (Math/toRadians (- lat2 lat1))
        dlon (Math/toRadians (- lon2 lon1))
        lat1 (Math/toRadians lat1)
        lat2 (Math/toRadians lat2)
        cos-lat1 (Math/cos lat1)
        cos-lat2 (Math/cos lat2)
        sin-half-dlat (Math/sin (/ dlat 2))
        sin-half-dlon (Math/sin (/ dlon 2))
        a (+ (* sin-half-dlat sin-half-dlat)
             (* sin-half-dlon sin-half-dlon cos-lat1 cos-lat2))]
    (* R 2 (Math/asin (Math/sqrt a)))))

(defn assoc-distance
  [lat lon m]
  (let [distance (haversine {:lat lat :lon lon} {:lat (:lat m) :lon (:lon m)})]
    (assoc m :distance distance)))
