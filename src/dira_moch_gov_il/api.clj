(ns dira-moch-gov-il.api
  (:require (clj-http [client :as http]
                      [cookies :as cookie])
            [ring.util.codec :refer [form-encode]]))

(def ^:private site-cookie
  (delay
    (let [cs (cookie/cookie-store)]
      (http/get "https://dira.moch.gov.il" {:cookie-store cs})
      cs)))

(def query
  (fn [method {:as where}]
    (-> (http/get
          "https://dira.moch.gov.il/api/Invoker"
          {:query-params {:method (name method)
                          :param (when where (str "?" (form-encode where)))}
           :cookie-store @site-cookie
           :as :json})
        :body)))

(comment
  (query :LotteryResult {:lotteryNumber 295})
  (query :Projects {:PageNumber 1 :PageSize 10}))
